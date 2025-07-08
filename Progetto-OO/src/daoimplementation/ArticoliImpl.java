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
                SELECT 
                    c.codcliente,
                    c.nome,
                    c.cognome,
                    p.categoria,
                    COALESCE(SUM(ao.prezzo * ao.numeroarticoli * 0.10), 0) as punti_categoria,
                    COALESCE(SUM(ao.prezzo * ao.numeroarticoli), 0) as spesa_totale_categoria,
                    COUNT(DISTINCT o.codordine) as ordini_nella_categoria
                FROM cliente c
                INNER JOIN ordine o ON c.codcliente = o.codcliente
                INNER JOIN articoliordine ao ON o.codordine = ao.codordine
                INNER JOIN prodotto p ON ao.codprodotto = p.codprodotto
                GROUP BY c.codcliente, c.nome, c.cognome, p.categoria
                ORDER BY c.codcliente, p.categoria
                """;

        try (Statement searchClient = connection.createStatement();
             ResultSet rs = searchClient.executeQuery(query)) {

            while (rs.next()) {
                // Creiamo un oggetto Articoli per memorizzare i dati della ricerca
                // codProdotto -> categoria
                // prezzo -> punti_categoria  
                // numeroArticoli -> ordini_nella_categoria
                // codCliente -> spesa_totale_categoria (convertita a int per semplicità)
                Articoli articoliInfo = new Articoli(
                        null, // codOrdine non necessario
                        rs.getString("categoria"), // categoria nel campo codProdotto
                        rs.getDouble("punti_categoria"), // punti categoria
                        rs.getInt("ordini_nella_categoria"), // ordini nella categoria
                        (int)rs.getDouble("spesa_totale_categoria") // spesa totale (convertita a int)
                );
                
                clienti.add(new Cliente(
                        rs.getString("codcliente"), 
                        rs.getString("nome"), 
                        rs.getString("cognome"), 
                        null, null, null, null, null,
                        articoliInfo
                ));
            }
        }
        return clienti;
    }
}
