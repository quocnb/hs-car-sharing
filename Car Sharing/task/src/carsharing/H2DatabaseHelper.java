package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class H2DatabaseHelper {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";

    public static void setDbFileName(String dbFileName) {
        if (dbFileName == null || dbFileName.isEmpty()) {
            dbFileName = "carsharing";
        }
        DB_URL = String.format("jdbc:h2:./src/carsharing/db/%s", dbFileName);
    }

    public static void connectAndExecuteSql(List<String> sqlList) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            conn = DriverManager.getConnection(DB_URL,"","");
            conn.setAutoCommit(true);
            //STEP 3: Execute a query
            stmt = conn.createStatement();
            for (String sql: sqlList) {
                stmt.executeUpdate(sql);
            }

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
                System.err.println(se2.getMessage());
            }
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                System.err.println(se.getMessage());
            }
        }
    }
}
