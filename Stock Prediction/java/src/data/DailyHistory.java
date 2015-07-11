package data;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class DailyHistory {
    // symbol of the stock
    String symbol;
    // parsed and adjusted historical data
    int[] id;
    String[] date;
    double[] open;
    double[] high;
    double[] low;
    double[] close;
    int[] volume;
    // the number of days
    int N;
    // original data
    ArrayList<String> original;
    
    public DailyHistory(String s) {
        symbol = s;
    }
    
    public void downloadHistory() {
        String site = "http://ichart.yahoo.com/table.csv?s=" + symbol;
        Scanner scanner = getConnection(site);
        parseHistory(scanner);
    }
    
    // download historical data from yahoo finance
    private static Scanner getConnection(String address) {
        Scanner s = null;
        try {
            URL url = new URL(address);
            s = new Scanner(url.openStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    // parse data;
    // Dividend and split adjusted for price and volume forward;
    private void parseHistory(Scanner scanner) {
        original = new ArrayList<String>();
        while (scanner.hasNext())
            original.add(scanner.nextLine());
        N = original.size() - 1;
        double adjFactor = 1.0;

        date = new String[N];
        open = new double[N];
        high = new double[N];
        low = new double[N];
        close = new double[N];
        volume = new int[N];

        for (int i = 1; i <= N; i++) {
            String[] parts = original.get(i).split(",");
            date[i - 1] = parts[0];
            adjFactor = Double.parseDouble(parts[6])
                    / Double.parseDouble(parts[4]);
            open[i - 1] = Double.parseDouble(parts[1]) * adjFactor;
            high[i - 1] = Double.parseDouble(parts[2]) * adjFactor;
            low[i - 1] = Double.parseDouble(parts[3]) * adjFactor;
            close[i - 1] = Double.parseDouble(parts[6]);
            volume[i - 1] = Integer.parseInt(parts[5]);
        }
    }

    // save data to a file as symbol.csv
    public void dataToFile() {
        Writer writer = null;
        // save processed data
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("history/"
                            + symbol + "_processed.csv"), "utf-8"));
            for (int i = 0; i < N; i++) {
                writer.write(String.format(
                        "%s,%10.3f,%10.3f,%10.3f,%10.3f,%10d%n", date[i],
                        open[i], high[i], low[i], close[i], volume[i]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // save original data
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("history/" + symbol + "_original.csv"),
                    "utf-8"));
            for (int i = 0; i < N; i++) {
                writer.write(original.get(i) + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // initiate stock history 
    public void dataToDB(String dbName) {
        createTable(dbName);
        insertData(dbName);
    }
    
    // create a table of the stock
    private void createTable(String dbName) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + symbol + "_processed " +
                         "(id    INTEGER PRIMARY KEY          , " +
                         "date   TEXT                 NOT NULL, " +
                         "open   REAL                         , " +
                         "high   REAL                         , " +
                         "low    REAL                         , " +
                         "close  REAL                 NOT NULL, " +
                         "volume INT              )";
            stmt.executeUpdate(sql);
                      
            sql = "CREATE TABLE " + symbol + "_original "
                    + "(id    INTEGER PRIMARY KEY         , "
                    + "line   TEXT                NOT NULL)";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    // save data to sql db
    private void insertData(String dbName) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            // store the adjusted data  
            for (int i = N-1; i >= 0; i--) {
                String sql = "INSERT INTO " + symbol + "_processed "
                        + "VALUES (" + (N-i) + ", '" + date[i] + "', "
                        + open[i] + ", " + high[i] + ", " + low[i] + ", "
                        + close[i] + ", " + volume[i] + ")";
                stmt.executeUpdate(sql);
           
            }
            // store the original csv data
            // note the size of original is N+1 and the first line is the titles
            for (int i = N; i >= 1; i--) {
                String sql = "INSERT INTO " + symbol + "_original " + 
                "VALUES (" + (N+1-i) + ", '" + original.get(i) + "')";
                stmt.executeUpdate(sql);
            }
            // update the history_info table
            // need to check uniqueness of the stock later
            String sql = "INSERT INTO history_info (stock, first, last, length) " + 
                         "VALUES ('" + symbol + "', '" + date[N-1] + "', '" + date[0] +
                         "', " + N + ")";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    // load history from the database and initialize the instance variable
    public void loadHistory(String dbName) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            stmt = c.createStatement();
            
            String sql = "SELECT length from history_info "
                       + "WHERE stock = '" + symbol + "';";
            ResultSet rs = stmt.executeQuery(sql);
            int length = rs.getInt("length");
            N = length;
            id = new int[length];
            date = new String[length];
            open = new double[length];
            high = new double[length];
            low = new double[length];
            close = new double[length];
            volume = new int[length];
            
            sql = "SELECT id, date, open, high, low, close, volume "
                + "FROM " + symbol + "_processed ";
            rs = stmt.executeQuery(sql);
            int i = N - 1;
            while (rs.next()) {
                id[i] = rs.getInt("id");
                date[i] = rs.getString("date");
                open[i] = rs.getDouble("open");
                high[i] = rs.getDouble("high");
                low[i] = rs.getDouble("low");
                close[i] = rs.getDouble("close");
                volume[i] = rs.getInt("volume");
                i--;
            }
            
            stmt.close();
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // update history in database
    // to be implemented
    public void updateDB() {
        //
    }
    
    public static void test() {
        DailyHistory dh = new DailyHistory("aapl");
        dh.loadHistory("wavechaser");
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        test();
    }

}
