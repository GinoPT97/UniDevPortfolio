package JDBCimplementazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import JDBC.ArticoliJDBC;
import Model.Articoli;
import Model.Cliente;

public class ArticoliImpl implements ArticoliJDBC {

	private Connection connection;
	private PreparedStatement newarticoli;
	private Statement searchClient;

	public ArticoliImpl(Connection connection) throws SQLException {
		this.connection = connection;
		newarticoli = connection.prepareStatement("INSERT INTO articoliordine VALUES (?, ?, ?, ?, ?, ?)");
		searchClient = connection.createStatement();
	}

	@Override
	public boolean newordine(Articoli articoli) throws SQLException {
		newarticoli.setString(1, articoli.getCodOrdine());
		newarticoli.setString(2, articoli.getCodProdotto());
		newarticoli.setDouble(3, articoli.getPrezzo());
		newarticoli.setDouble(4, articoli.getNumPunti());
		newarticoli.setInt(5, articoli.getNumeroArticoli());
		newarticoli.setString(6, articoli.getCategoria());
		int row = newarticoli.executeUpdate();
		if (row < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public ArrayList<Cliente> SearchClient() throws SQLException {
		ArrayList<Cliente> clienti = new ArrayList<>();
		ResultSet rs = searchClient.executeQuery(
				"SELECT C.codcliente, C.nome, C.cognome, AO.categoria, SUM(AO.numeropunti) AS total_punti "
						+ "FROM cliente AS C " + "JOIN articoliordine AS AO ON C.codcliente = AO.codcliente "
						+ "GROUP BY C.codcliente, C.nome, C.cognome, AO.categoria;");
		while (rs.next()) {
			clienti.add(new Cliente(null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null,
					new Articoli(null, null, 0.0, rs.getDouble("total_punti"), 0, rs.getString("categoria"))));
		}
		rs.close();
		return clienti;
	}
}
