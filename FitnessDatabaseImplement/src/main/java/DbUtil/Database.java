package DbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String host = "192.168.1.97";
    private static final String database = "FitnessCoursework";
    private static final int port = 5434;
    private static final String user = "postgres";
    private static final String pass = "1234";


    //    private static final String host = "localhost";
//    private static final String database = "Fitness";
//    private static final int port = 5432;
//    private static final String user = "postgres";
//    private static final String pass = "postgres";
    private static String url = "jdbc:postgresql://%s:%d/%s";


    private static Connection dbConnection = null;

//    public Database() {
//        this.url = String.format(this.url, this.host, this.port, this.database);
//        //this.disconnect();
//        System.out.println("connection status:" + status);
//    }

    public static Connection getConnection() {
        if (dbConnection == null) {
            try {
                url = String.format(url, host, port, database);
                Class.forName("org.postgresql.Driver");
                dbConnection = DriverManager.getConnection(url, user, pass);
                dbConnection.setAutoCommit(false);
                System.out.println("connection status:" + true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbConnection;
    }

    public static void destroy() {
        try {
            if (dbConnection != null)
                dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
