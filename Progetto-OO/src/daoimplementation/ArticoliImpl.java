package daoimplementation;

import daointerface.ArticoliJDBC;
import java.sql.*;
import java.util.ArrayList;
import model.Articoli;
import model.Cliente;

public class ArticoliImpl implements ArticoliJDBC {

    private final Connection connection;

    public ArticoliImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean newordine(Articoli articoli) throws SQLException {
        String query = "INSERT INTO articoliordine (codordine, codprodotto, prezzo, numeroarticoli) VALUES (CAST(? AS INTEGER), CAST(? AS INTEGER), ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(articoli.getCodOrdine()));
            stmt.setInt(2, Integer.parseInt(articoli.getCodProdotto()));
            stmt.setDouble(3, articoli.getPrezzo());
            stmt.setInt(4, articoli.getNumeroArticoli());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new SQLException("Errore di conversione: codOrdine o codProdotto non è un intero valido.", e);
        }
    }

    @Override
    public ArrayList<Cliente> searchClient() throws SQLException {
        ArrayList<Cliente> clienti = new ArrayList<>();
        String query = """
                SELECT C.codcliente, C.nome, C.cognome, P.categoria, SUM(AO.prezzo * AO.numeroarticoli) AS total_punti
                FROM cliente AS C
                JOIN ordine AS O ON C.codcliente = O.codcliente
                JOIN articoliordine AS AO ON O.codordine = AO.codordine
                JOIN prodotto AS P ON AO.codprodotto = P.codprodotto
                GROUP BY C.codcliente, C.nome, C.cognome, P.categoria
                """;

        try (Statement searchClient = connection.createStatement();
             ResultSet rs = searchClient.executeQuery(query)) {

            while (rs.next()) {
                clienti.add(new Cliente(
                        null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null,
                        new Articoli(null, null, rs.getDouble("total_punti"), 0, rs.getInt("codcliente"))
                ));
            }
        }
        return clienti;
    }
}
