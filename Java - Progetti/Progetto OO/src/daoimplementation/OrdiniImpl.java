package daoimplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import Model.Ordine;
import daointerface.OrdiniJDBC;

public class OrdiniImpl implements OrdiniJDBC {
    private final Connection connection;
    private PreparedStatement newOrdineStmt;
    private PreparedStatement getAllOrdiniStmt;

    public OrdiniImpl(Connection connection) throws SQLException {
        this.connection = connection;
        // Preparazione delle query
        newOrdineStmt = connection.prepareStatement("INSERT INTO ordine (prezzototale, dataacquisto, codcliente, coddipendente) VALUES (?, ?, ?, ?)");
        getAllOrdiniStmt = connection.prepareStatement("SELECT codordine, dataacquisto, prezzototale, codcliente, coddipendente FROM ordine ORDER BY dataacquisto DESC");
    }

    @Override
    public boolean newordine(Ordine ordine) throws SQLException {
        newOrdineStmt.setDouble(1, ordine.getPrezzoTotale());
        newOrdineStmt.setDate(2, ordine.getDataAcquisto());
        newOrdineStmt.setInt(3, ordine.getIdCliente());
        newOrdineStmt.setInt(4, ordine.getIdDipendente());

        return newOrdineStmt.executeUpdate() > 0;
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
                    rs.getInt("codcliente"),
                    rs.getInt("coddipendente")
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
            Logger.getLogger(OrdiniImpl.class.getName()).log(Level.SEVERE, "Errore durante l'esecuzione della query", e);
        }
        return null;
    }

    @Override
    public String getCurrentCod() throws SQLException {
        String query = "SELECT currval(pg_get_serial_sequence('ordine', 'codordine')) AS codordine";
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






