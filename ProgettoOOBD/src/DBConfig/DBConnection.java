package DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final String USERNAME = "admin";
    private final String PASSWORD = "Admin";
    private final String IP = "localhost"; //To do
    private final String PORT = "5432";
    private String URL = "jdbc:postgresql://";
    private static DBConnection instance = null;
    private Connection connection = null;

    private DBConnection (String DB) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL + IP + ":" + PORT + "/" + DB, USERNAME, PASSWORD);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Database Connection Creation Failed: "+e.getMessage());
        }
    }

    public Connection getConnection() { return connection; }

    public static DBConnection getInstance(String DB) throws SQLException {
        if (instance == null) {
            instance = new DBConnection(DB);
        }
        else if (instance.getConnection().isClosed()) {
            instance = new DBConnection(DB);
        }
        return instance;
    }
}
