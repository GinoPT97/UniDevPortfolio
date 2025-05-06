package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAOInterface.ArticoliJDBC;
import Model.Articoli;
import Model.Cliente;

public class ArticoliImpl implements ArticoliJDBC {

    private final Connection connection;

    // Costruttore
    public ArticoliImpl(Connection connection) throws SQLException {
        this.connection = connection;
    }

    @Override
    public boolean newordine(Articoli articoli) throws SQLException {
        String query = "INSERT INTO articoliordine (CodOrdine, CodProdotto, Prezzo, NumeroPunti, NumeroArticoli, Categoria, CodCliente) VALUES (CAST(? AS INTEGER), CAST(? AS INTEGER), ?, ?, ?, CAST(? AS TIPOLOGIA), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(articoli.getCodOrdine())); // Convert codOrdine to integer
            stmt.setInt(2, Integer.parseInt(articoli.getCodProdotto())); // Convert codProdotto to integer
            stmt.setDouble(3, articoli.getPrezzo());
            stmt.setDouble(4, articoli.getNumPunti());
            stmt.setInt(5, articoli.getNumeroArticoli());
            stmt.setString(6, articoli.getCategoria()); // Ensure categoria matches the TIPOLOGIA enum values
            stmt.setInt(7, articoli.getCodCliente()); // Add codCliente

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new SQLException("Errore di conversione: codOrdine o codProdotto non è un intero valido.", e);
        }
    }

    @Override
    public ArrayList<Cliente> SearchClient() throws SQLException {
        ArrayList<Cliente> clienti = new ArrayList<>();
        String query = """
                SELECT C.codcliente, C.nome, C.cognome, AO.categoria, SUM(AO.numeropunti) AS total_punti
                FROM cliente AS C
                JOIN articoliordine AS AO ON C.codcliente = AO.codcliente
                GROUP BY C.codcliente, C.nome, C.cognome, AO.categoria
                """;

        try (Statement searchClient = connection.createStatement();
             ResultSet rs = searchClient.executeQuery(query)) {

            while (rs.next()) {
                clienti.add(new Cliente(
                        null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null,
                        new Articoli(null, null, 0.0, rs.getDouble("total_punti"), 0, rs.getString("categoria"), rs.getInt("codcliente")) // Fix constructor
                ));
            }
        }
        return clienti;
    }
}

