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

    private final PreparedStatement newtesseraStmt;
    private final PreparedStatement getpuntitStmt;
    private final PreparedStatement alltesseraStmt;

    private final Connection connection;

    public Tesseraimpl(Connection connection) throws SQLException {
        this.connection = connection;
        // Preparazione delle query
        newtesseraStmt = connection.prepareStatement("INSERT INTO tessera (numeropunti, codcliente, dataemissione, datascadenza, stato) VALUES (?, ?, CURRENT_DATE, CURRENT_DATE + INTERVAL '2 years', 'ATTIVA')");
        getpuntitStmt = connection.prepareStatement("SELECT numeropunti FROM tessera WHERE codtessera = ?");
        alltesseraStmt = connection.prepareStatement("SELECT T.codtessera, T.numeropunti, T.codcliente, T.dataemissione, T.datascadenza, T.stato, C.nome, C.cognome, C.codicefiscale, C.email, C.indirizzo, C.telefono FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente ORDER BY C.cognome DESC");
    }

    @Override
    public boolean newtessera(String codcl) throws SQLException {
        if (codcl == null) return false; // Non può creare tessera senza codice cliente

        newtesseraStmt.setDouble(1, 0.00);  // Tessera inizialmente con 0 punti
        newtesseraStmt.setInt(2, Integer.parseInt(codcl)); // Conversione String -> int per codcliente

        return newtesseraStmt.executeUpdate() > 0;  // Restituisce true se è stata inserita una riga
    }

    @Override
    public String getpuntit(String codt) throws SQLException {
        getpuntitStmt.setString(1, codt);
        try (ResultSet rs = getpuntitStmt.executeQuery()) {
            if (rs.next())
				return String.valueOf(rs.getDouble("numeropunti"));  // Converto il valore in String
        }
        return null;  // Se non ci sono risultati
    }

    @Override
    public ArrayList<Tessera> alltessera() throws SQLException {
        ArrayList<Tessera> tessere = new ArrayList<>();
        try (ResultSet rs = alltesseraStmt.executeQuery()) {
            while (rs.next())
				// Creo e aggiungo una nuova Tessera all'elenco
                tessere.add(new Tessera(
                        rs.getString("codtessera"),
                        rs.getDouble("numeropunti"),
                        rs.getDate("dataemissione") != null ? rs.getDate("dataemissione").toLocalDate() : null,
                        rs.getDate("datascadenza") != null ? rs.getDate("datascadenza").toLocalDate() : null,
                        rs.getString("stato"),
                        new Cliente(
                                rs.getString("codcliente"),
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
        return tessere;
    }

    @Override
    public boolean updatepunti(String codCliente, double punti) throws SQLException {
        String query = "UPDATE tessera SET numeropunti = numeropunti + ? WHERE codcliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, punti);
            try {
                // Try to parse as integer since database expects INTEGER
                int clienteId = Integer.parseInt(codCliente);
                stmt.setInt(2, clienteId);
            } catch (NumberFormatException e) {
                // Fallback: use as string and let database handle conversion
                stmt.setString(2, codCliente);
            }
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento punti per cliente " + codCliente + ": " + e.getMessage());
            throw e;
        }
    }

}

