package DBConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// La classe DBConnection rappresenta una connessione a un database PostgreSQL.

public class DBConnection {
	public static DBConnection instance;
	private Connection connection = null;
	private final String USERNAME = "postgres";
	private final String PASSWORD = "admin";
	private final String IP = "localhost";
	private final String PORT = "5432";
	private String url = "jdbc:postgresql://" + IP + ":" + PORT + "/";

	// Costruisce un nuovo oggetto DBConnection con il nome del database specificato.
	
	private DBConnection(String db) throws SQLException {

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url + db, USERNAME, PASSWORD);
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
	public static DBConnection getInstance(String db) throws SQLException {
		if (instance == null) {
			instance = new DBConnection(db);
		} else if (instance.getConnection().isClosed()) {
			instance = new DBConnection(db);
		}

		return instance;
	}
}
