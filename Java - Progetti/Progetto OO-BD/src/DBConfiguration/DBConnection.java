package DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBConnection {
    public static DBConnection instance;
	private Connection connection = null;
    private final String USERNAME = "postgres";
    private final String PASSWORD = "admin";
    private final String IP = "localhost";
    private final String PORT = "5432";
    private String url = "jdbc:postgresql://"+IP+":"+PORT + "/";
    
    private DBConnection(String db) throws SQLException {

        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url+db, USERNAME, PASSWORD);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance(String db) throws SQLException {
        if (instance == null)
        {
            instance = new DBConnection(db);
        }
        else
            if (instance.getConnection().isClosed())
            {
                instance = new DBConnection(db);
            }

        return instance;
    }
}
