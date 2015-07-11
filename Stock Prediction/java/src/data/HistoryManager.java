package data;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HistoryManager {
    public static final String PROJECT_DB = "wavechaser";
    
    public static void initiateProjectDB() {
        initiateDB(PROJECT_DB);
        StockSet.addDowComposites(PROJECT_DB);
        initiateHistory(PROJECT_DB, "dow_composites");
    }
    
    // initiate a database and create an empty table for storing history info.
     public static void initiateDB(String dbName) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            String sql = "CREATE TABLE history_info  "
                    + "(id    INTEGER PRIMARY KEY     ,"
                    + " stock TEXT            NOT NULL,"
                    + " first TEXT                    ,"
                    + " last  TEXT                    ,"
                    + " length INT                    )";
            stmt.executeUpdate(sql);
            
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // download and store history of stocks as a table in database
    public static void initiateHistory(String dbName, String stockTable) {
        ArrayList<String> stocks = getStocks(dbName, stockTable);
        for (int i = 0; i < stocks.size(); i++) {
            String stock = stocks.get(i);
            DailyHistory dh = new DailyHistory(stock);
            dh.downloadHistory();
            dh.dataToDB(dbName);
        }
    }
    private static ArrayList<String> getStocks(String dbName, String stockTable) {
        ArrayList<String> stocks = new ArrayList<String>();
        Connection con = null;
        Statement sta = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            con.setAutoCommit(false);
            sta = con.createStatement();
            
            ResultSet rs = sta.executeQuery("SELECT stock FROM " + stockTable);
            while (rs.next()) {
                stocks.add(rs.getString("stock"));
            }
            
            sta.close();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return stocks;
    }
    
    // update History in database
    // tbi
    public static void updateHistory(String dbName, String companyTables) {
        
    }
    
    // get history
    // if the stock is in the database, load history from the database;
    // else download the history and store the database, then load from it.
    public static void loadHistory(String dbName, Stock stock) {
        boolean contains = false;
        
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            stmt = c.createStatement();
            
            String sql = "SELECT count(*) from history_info "
                       + "WHERE stock = '" + stock.symbol + "';";
            ResultSet rs = stmt.executeQuery(sql);
            contains = rs.getBoolean(1);
            
            stmt.close();
            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        DailyHistory dh = new DailyHistory(stock.symbol);
        if (!contains) {
            dh.downloadHistory();
            dh.dataToDB(dbName);
        }
        dh.loadHistory(dbName);
        stock.N = dh.N;
        stock.id = dh.id;
        stock.date = dh.date;
        stock.open = dh.open;
        stock.high = dh.high;
        stock.low = dh.low;
        stock.close = dh.close;
        stock.volume = dh.volume;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        initiateProjectDB();
        
    }

}
