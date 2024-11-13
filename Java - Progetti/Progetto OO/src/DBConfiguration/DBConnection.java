package DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//La classe DBConnection rappresenta una connessione a un database PostgreSQL.

public class DBConnection {
    public static DBConnection instance;
    private Connection connection = null;
    
    // Credenziali per il database locale
    private final String LOCAL_USERNAME = "postgres";
    private final String LOCAL_PASSWORD = "admin";
    private final String LOCAL_URL = "jdbc:postgresql://localhost:5432/";

    // Credenziali per Supabase
    private final String SUPABASE_USERNAME = "postgres.rgdueknjajsikdwrxrga";
    private final String SUPABASE_PASSWORD = "zjjtoXltLwSP0RTp";
    private final String SUPABASE_URL = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:6543/";

    // Costruisce un nuovo oggetto DBConnection con il nome del database e il tipo di connessione
    private DBConnection(String db, boolean useSupabase) throws SQLException {
        String url;
        String username;
        String password;

        if (useSupabase) {
            url = SUPABASE_URL + db;
            username = SUPABASE_USERNAME;
            password = SUPABASE_PASSWORD;
        } else {
            url = LOCAL_URL + db;
            username = LOCAL_USERNAME;
            password = LOCAL_PASSWORD;
        }

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Creazione della connessione al database fallita: " + ex.getMessage());
        }
    }

    // Restituisce la connessione al database
    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance(String db, boolean useSupabase) throws SQLException {
        if (instance == null) {
            instance = new DBConnection(db, useSupabase);
        } else if (instance.getConnection().isClosed()) {
            instance = new DBConnection(db, useSupabase);
        }
        return instance;
    }
}

