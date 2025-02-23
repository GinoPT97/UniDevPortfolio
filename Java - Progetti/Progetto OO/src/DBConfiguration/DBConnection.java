package DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection = null;

    private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/";
    private final String LOCAL_USERNAME = "postgres";
    private final String LOCAL_PASSWORD = "postgres";

    private DBConnection(boolean useSupabase) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(LOCAL_URL, LOCAL_USERNAME, LOCAL_PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.out.println("Creazione della connessione al database fallita: " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance(boolean useSupabase) throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection(useSupabase);
        }
        return instance;
    }
}


