package dbconfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final String DB_URL;
    private static final String DB_USERNAME;
    private static final String DB_PASSWORD;

    static {
        DB_URL = "jdbc:postgresql://localhost:5432/";
        DB_USERNAME = "postgres";
        DB_PASSWORD = "postgres";
    }

    private final Connection connection;

    private DBConnection() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to establish database connection to " + DB_URL, e);
        }
    }

    public static DBConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null)
			try {
                connection.close();
                LOGGER.info("Database connection closed");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database connection", e);
            }
    }

    private static class Holder {
        private static final DBConnection INSTANCE = new DBConnection();
    }
}
