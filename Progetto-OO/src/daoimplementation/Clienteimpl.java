package daoimplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import daointerface.ClienteJDBC;
import model.Cliente;
import model.Tessera;

public class Clienteimpl implements ClienteJDBC {
	private static final String CODCLIENTE = "codcliente";
	private final PreparedStatement setNewCt;
	private final PreparedStatement updateCl;
	private final Statement getAllCt;
	private final Statement idCl;

	// Costruttore
	public Clienteimpl(Connection connection) throws SQLException {
		getAllCt = connection.createStatement();
		setNewCt = connection.prepareStatement(
				"INSERT INTO cliente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES (?, ?, ?, ?, ?, ?)");
		updateCl = connection.prepareStatement(
				"UPDATE cliente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE codcliente = ?");
		idCl = connection.createStatement();
	}

	@Override
	public boolean setNewCt(Cliente cliente) throws SQLException {
		setPreparedStatement(setNewCt, cliente);
		return setNewCt.executeUpdate() > 0;
	}

	@Override
	public ArrayList<Cliente> getAllCt() throws SQLException {
		ArrayList<Cliente> clienti = new ArrayList<>();
		String query = "SELECT c.codcliente, c.nome, c.cognome, c.codicefiscale, c.email, c.indirizzo, c.telefono, "
				+ "t.codtessera, t.numeropunti, t.stato, t.datascadenza " + "FROM cliente c "
				+ "LEFT JOIN tessera t ON c.codcliente = t.codcliente " + "ORDER BY c.cognome DESC";

		try (ResultSet rs = getAllCt.executeQuery(query)) {
			while (rs.next()) {
				Tessera tessera = null;
				String codTessera = rs.getString("codtessera");
				if (codTessera != null) {
					java.sql.Date dataScadenzaSql = rs.getDate("datascadenza");
					java.time.LocalDate dataScadenza = dataScadenzaSql != null ? dataScadenzaSql.toLocalDate() : null;
					String stato = rs.getString("stato");
					tessera = new Tessera(codTessera, rs.getDouble("numeropunti"), null, // dataemissione non
																							// disponibile
							dataScadenza, stato, null // proprietario non necessario qui
					);
				}
				clienti.add(new Cliente(rs.getString(CODCLIENTE), rs.getString("nome"), rs.getString("cognome"),
						rs.getString("codicefiscale"), rs.getString("email"), rs.getString("indirizzo"),
						rs.getString("telefono"), tessera, null));
			}
		}
		return clienti;
	}

	@Override
	public String getIdCt(String codicefiscale) throws SQLException {
		try (ResultSet rs = idCl
				.executeQuery("SELECT " + CODCLIENTE + " FROM cliente WHERE codicefiscale = '" + codicefiscale + "'")) {
			if (rs.next())
				return rs.getString(CODCLIENTE); // Use constant
		}
		return null;
	}

	@Override
	public boolean updateCliente(Cliente cliente) throws SQLException {
		setPreparedStatement(updateCl, cliente);
		// Il codice cliente nel database è INTEGER, quindi convertiamo da String
		updateCl.setInt(7, Integer.parseInt(cliente.getCodCliente()));
		return updateCl.executeUpdate() > 0;
	}

	private void setPreparedStatement(PreparedStatement ps, Cliente cliente) throws SQLException {
		ps.setString(1, cliente.getNome());
		ps.setString(2, cliente.getCognome());
		ps.setString(3, cliente.getCodiceFiscale());
		ps.setString(4, cliente.getIndirizzo());
		ps.setString(5, cliente.getTelefono());
		ps.setString(6, cliente.getEmail());
	}
}
