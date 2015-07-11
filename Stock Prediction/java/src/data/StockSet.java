package data;
import java.sql.*;
public class StockSet {

    static final String[] dow = { "aapl", "axp", "ba", "cat", "csco", "cvx",
        "dd", "dis", "ge", "gs", "hd", "ibm", "intc", "jnj", "jpm", "ko",
        "mcd", "mmm", "mrk", "msft", "nke", "pfe", "pg", "trv", "unh",
        "utx", "v", "vz", "wmt", "xom" };

public static void addDowComposites(String dbName) {
    Connection c = null;
    Statement stmt = null;
    try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        c.setAutoCommit(false);
        stmt = c.createStatement();
        String sql = "CREATE TABLE dow_composites "
                + "(id    INTEGER PRIMARY KEY         , "
                + " stock TEXT                NOT NULL)";
        stmt.executeUpdate(sql);
        
        for (int i = 0; i < dow.length; i++) {
            sql = "INSERT INTO dow_composites " + 
                  "VALUES (" + i + ", '" + dow[i] + "')";
            stmt.executeUpdate(sql);
        }
        
        stmt.close();
        c.commit();
        c.close();
    } catch (Exception e){
        e.printStackTrace();
    }
}
public static void main(String[] args) {
    // TODO Auto-generated method stub
    addDowComposites("test");
}

}
