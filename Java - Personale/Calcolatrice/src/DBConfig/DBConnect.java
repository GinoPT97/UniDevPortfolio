package DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	private static DBConnect instance;
    private Connection connection = null;
    private final String USERNAME = "postgres";
    private final String PASSWORD = "Caramella17";
    private String url = "jdbc:postgresql://localhost:5432/";

    private DBConnect(String db) throws SQLException {

        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url+db, USERNAME, PASSWORD);
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnect getInstance(String db) throws SQLException {
        if (instance == null)
        {
            instance = new DBConnect(db);
        }
        else
            if (instance.getConnection().isClosed())
            {
                instance = new DBConnect(db);
            }

        return instance;
    }
}
