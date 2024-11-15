package DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// La classe DBConnection rappresenta una connessione a un database PostgreSQL.
public class DBConnection {
    public static DBConnection instance;
    private Connection connection = null;
    private final String LOCAL_USERNAME = "postgres";
    private final String LOCAL_PASSWORD = "admin";
    private final String LOCAL_IP = "localhost";
    private final String LOCAL_PORT = "5432";
    private final String LOCAL_URL = "jdbc:postgresql://" + LOCAL_IP + ":" + LOCAL_PORT + "/";

    private final String SUPABASE_URL = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:6543/postgres";
    private final String SUPABASE_USERNAME = "postgres.rgdueknjajsikdwrxrga";
    private final String SUPABASE_PASSWORD = "zjjtoXltLwSP0RTp";

    private boolean useSupabase = false;

    // Costruisce un nuovo oggetto DBConnection con il nome del database specificato.
    private DBConnection(String db) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            if (useSupabase) {
                // Connessione a Supabase
                connection = DriverManager.getConnection(
                    SUPABASE_URL + "?user=" + SUPABASE_USERNAME + "&password=" + SUPABASE_PASSWORD
                );
            } else {
                // Connessione locale
                connection = DriverManager.getConnection(LOCAL_URL + db, LOCAL_USERNAME, LOCAL_PASSWORD);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Creazione della connessione al database fallita: " + ex.getMessage());
        }
    }

    // Restituisce la connessione al database.
    public Connection getConnection() {
        return connection;
    }

    /**
     * Restituisce l'istanza singleton della classe DBConnection. Se l'istanza è
     * nulla o la connessione è chiusa, viene creata una nuova istanza.
     */
    public static DBConnection getInstance(String db, boolean useSupabase) throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection(db);
            instance.useSupabase = useSupabase;
        }
        return instance;
    }
}

