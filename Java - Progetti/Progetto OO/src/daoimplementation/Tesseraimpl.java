package daoimplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import daointerface.TesseraJDBC;
import model.Cliente;
import model.Tessera;

public class Tesseraimpl implements TesseraJDBC {

    private PreparedStatement newtesseraStmt;
    private PreparedStatement getpuntitStmt;
    private PreparedStatement alltesseraStmt;

    private Connection connection;

    public Tesseraimpl(Connection connection) throws SQLException {
        this.connection = connection;
        // Preparazione delle query
        newtesseraStmt = connection.prepareStatement("INSERT INTO tessera (numeropunti, codcliente) VALUES (?, ?)");
        getpuntitStmt = connection.prepareStatement("SELECT numeropunti FROM tessera WHERE codtessera = ?");
        alltesseraStmt = connection.prepareStatement("SELECT * FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente ORDER BY C.cognome DESC");
    }

    @Override
    public boolean newtessera(String codcl) throws SQLException {
        newtesseraStmt.setDouble(1, 0.00);  // Tessera inizialmente con 0 punti
        newtesseraStmt.setString(2, codcl); // Codice cliente associato
        return newtesseraStmt.executeUpdate() > 0;  // Restituisce true se è stata inserita una riga
    }

    @Override
    public String getpuntit(String codt) throws SQLException {
        getpuntitStmt.setString(1, codt);
        try (ResultSet rs = getpuntitStmt.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getDouble("numeropunti"));  // Converto il valore in String
            }
        }
        return null;  // Se non ci sono risultati
    }

    @Override
    public ArrayList<Tessera> alltessera() throws SQLException {
        ArrayList<Tessera> tessere = new ArrayList<>();
        try (ResultSet rs = alltesseraStmt.executeQuery()) {
            while (rs.next()) {
                // Creo e aggiungo una nuova Tessera all'elenco
                tessere.add(new Tessera(
                    rs.getString("codtessera"),
                    rs.getInt("numeropunti"),
                    new Cliente(
                        null,
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        null,
                        null
                    )
                ));
            }
        }
        return tessere;
    }

    @Override
    public boolean updatepunti(String codCliente, double punti) throws SQLException {
        String query = "UPDATE tessera SET numeropunti = numeropunti + ? WHERE codcliente = CAST(? AS INTEGER)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, punti);
            stmt.setInt(2, Integer.parseInt(codCliente)); // Ensure codCliente is cast to INTEGER
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new SQLException("Errore di conversione: codCliente non è un intero valido.", e);
        }
    }
}

