package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DAOInterface.ArticoliJDBC;
import Model.Articoli;
import Model.Cliente;

public class ArticoliImpl implements ArticoliJDBC {

    private final PreparedStatement newArticoli;
    private final Connection connection;

    // Costruttore
    public ArticoliImpl(Connection connection) throws SQLException {
        this.connection = connection;
        this.newArticoli = connection.prepareStatement(
                "INSERT INTO articoliordine (codOrdine, codProdotto, prezzo, numPunti, numeroArticoli, categoria) VALUES (?, ?, ?, ?, ?, ?)");
    }

    @Override
    public boolean newordine(Articoli articoli) throws SQLException {
        boolean result;
        setArticoliPreparedStatement(newArticoli, articoli);
        result = newArticoli.executeUpdate() > 0;
        return result;
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
                        new Articoli(null, null, 0.0, rs.getDouble("total_punti"), 0, rs.getString("categoria"))
                ));
            }
        }
        return clienti;
    }

    // Metodo di supporto per riempire il PreparedStatement
    private void setArticoliPreparedStatement(PreparedStatement ps, Articoli articoli) throws SQLException {
        ps.setString(1, articoli.getCodOrdine());
        ps.setString(2, articoli.getCodProdotto());
        ps.setDouble(3, articoli.getPrezzo());
        ps.setDouble(4, articoli.getNumPunti());
        ps.setInt(5, articoli.getNumeroArticoli());
        ps.setString(6, articoli.getCategoria());
    }
}

