package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import DAOInterface.OrdiniJDBC;
import Model.Ordine;

public class OrdiniImpl implements OrdiniJDBC {
    private final Connection connection;
    private PreparedStatement newOrdineStmt;
    private PreparedStatement getAllOrdiniStmt;

    public OrdiniImpl(Connection connection) throws SQLException {
        this.connection = connection;
        // Preparazione delle query
        newOrdineStmt = connection.prepareStatement("INSERT INTO ordine VALUES (NEXTVAL('SCodOrdine'), ?, ?, ?, ?)");
        getAllOrdiniStmt = connection.prepareStatement("SELECT * FROM ordine ORDER BY dataacquisto DESC");
    }

    @Override
    public boolean newordine(Ordine ordine) throws SQLException {
        newOrdineStmt.setDouble(1, ordine.getPrezzoTotale());
        newOrdineStmt.setDate(2, ordine.getDataAcquisto());
        newOrdineStmt.setString(3, ordine.getIdCliente());
        newOrdineStmt.setString(4, ordine.getIdDipendente());

        return newOrdineStmt.executeUpdate() > 0; // Restituisce true se l'inserimento ha avuto successo
    }

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

    @Override
    public String getOldDate() throws SQLException {
        String query = "SELECT MIN(dataacquisto) AS old FROM ordine";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                Date oldDate = rs.getDate("old");
                return (oldDate != null) ? oldDate.toString() : null;
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'esecuzione della query: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getCurrentCod() throws SQLException {
        String query = "SELECT currval('SCodOrdine') AS codordine";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("codordine");
            }
        }
        return null;
    }

    // Metodo per chiudere le risorse
    public void close() throws SQLException {
        if (newOrdineStmt != null) newOrdineStmt.close();
        if (getAllOrdiniStmt != null) getAllOrdiniStmt.close();
        if (connection != null) connection.close(); // Chiudo la connessione
    }
}






