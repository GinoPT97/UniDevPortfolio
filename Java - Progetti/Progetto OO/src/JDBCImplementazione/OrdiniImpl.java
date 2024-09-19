package JDBCImplementazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import JDBC.OrdiniJDBC;
import Model.Ordine;

public class OrdiniImpl implements OrdiniJDBC {
    private Connection connection;
    private PreparedStatement newOrdineStmt, getAllOrdiniStmt;
    private Statement oldDateStmt, currentCodStmt;

    // Costruttore per inizializzare la connessione e le query preparate
    public OrdiniImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.newOrdineStmt = connection.prepareStatement("INSERT INTO ordine VALUES (NEXTVAL('SCodOrdine'), ?, ?, ?, ?)");
        this.getAllOrdiniStmt = connection.prepareStatement("SELECT * FROM ordine ORDER BY dataacquisto DESC");
        this.oldDateStmt = connection.createStatement();
        this.currentCodStmt = connection.createStatement();
    }

    // Metodo per inserire un nuovo ordine
    @Override
    public boolean newordine(Ordine ordine) throws SQLException {
        newOrdineStmt.setDouble(1, ordine.getPrezzoTotale());
        newOrdineStmt.setDate(2, ordine.getDataAcquisto());
        newOrdineStmt.setString(3, ordine.getIdCliente());
        newOrdineStmt.setString(4, ordine.getIdDipendente());
        
        return newOrdineStmt.executeUpdate() > 0; // Restituisce true se l'inserimento ha avuto successo
    }

    // Metodo per recuperare tutti gli ordini
    @Override
    public ArrayList<Ordine> getallordini() throws SQLException {
        ArrayList<Ordine> ordiniList = new ArrayList<>();
        try (ResultSet rs = getAllOrdiniStmt.executeQuery()) {
            while (rs.next()) {
                ordiniList.add(new Ordine(
                    rs.getString("codordine"),
                    rs.getDate("dataacquisto"),
                    rs.getDouble("prezzototale"),
                    rs.getString("codcliente"),
                    rs.getString("coddipendente")
                ));
            }
        }
        return ordiniList;
    }

    // Metodo per ottenere la data più vecchia degli ordini
    @Override
    public String getOldDate() throws SQLException {
        String query = "SELECT MIN(dataacquisto) AS old FROM ordine";
        try (ResultSet rs = oldDateStmt.executeQuery(query)) {
            if (rs.next()) {
                Date oldDate = rs.getDate("old");
                return (oldDate != null) ? oldDate.toString() : null;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'esecuzione della query: " + e.getMessage());
        }
        return null;
    }

    // Metodo per ottenere il valore corrente del codice ordine
    @Override
    public String getCurrentCod() throws SQLException {
        String query = "SELECT currval('SCodOrdine') AS codordine";
        try (ResultSet rs = currentCodStmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("codordine");
            }
        }
        return null;
    }
}





