package JDBCimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import JDBC.ClienteJDBC;
import Model.Cliente;
import Model.Tessera;

public class Clienteimpl implements ClienteJDBC {
	private Connection connection;
	private PreparedStatement setNewCt, getAllCt, cercacl, updatecl;
	private Statement idcl;
	private ArrayList<Cliente> ctTot = new ArrayList<>();
	private Cliente ct;
	private String id = null;

	public Clienteimpl(Connection connection) throws SQLException {
		this.connection = connection;
		getAllCt = connection.prepareStatement(
				"SELECT * FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente  ORDER BY C.cognome DESC");
		cercacl = connection.prepareStatement(
				"SELECT codcliente FROM cliente WHERE nome = ? AND cognome = ? AND codicefiscale = ?");
		setNewCt = connection.prepareStatement("INSERT INTO cliente VALUES (nextval('SCodCliente'), ?, ?, ?, ?, ?, ?)");
		updatecl = connection.prepareStatement(
				"UPDATE cliente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE codcliente = ?");
		idcl = connection.createStatement();
	}

	@Override
	public boolean setNewCt(Cliente cliente) throws SQLException {
		setNewCt.setString(1, cliente.getNome());
		setNewCt.setString(2, cliente.getCognome());
		setNewCt.setString(3, cliente.getCodFis());
		setNewCt.setString(4, cliente.getInd());
		setNewCt.setString(5, cliente.getTel());
		setNewCt.setString(6, cliente.getEmail());
		int row = setNewCt.executeUpdate();
		if (row < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public ArrayList<Cliente> getAllCt() throws SQLException {
		ResultSet rs = getAllCt.executeQuery();
		while (rs.next()) {
			ctTot.add(new Cliente(rs.getString("codcliente"), rs.getString("nome"), rs.getString("cognome"),
					rs.getString("codicefiscale"), rs.getString("email"), rs.getString("indirizzo"),
					rs.getString("telefono"), new Tessera(rs.getString("codtessera"), rs.getInt("numeropunti"), null),
					null));
		}
		rs.close();
		return ctTot;
	}

	@Override
	public String getCtByNCCF(String name, String cognome, String codicefiscale) throws SQLException {
		cercacl.setString(1, name);
		cercacl.setString(2, cognome);
		cercacl.setString(3, codicefiscale);
		ResultSet rs = cercacl.executeQuery();
		String s = null;
		while (rs.next()) {
			s = rs.getString("codcliente");
		}
		rs.close();
		return s;
	}

	@Override
	public boolean updateCliente(Cliente cliente) throws SQLException {
		updatecl.setString(1, cliente.getNome());
		updatecl.setString(2, cliente.getCognome());
		updatecl.setString(3, cliente.getCodFis());
		updatecl.setString(4, cliente.getInd());
		updatecl.setString(5, cliente.getTel());
		updatecl.setString(6, cliente.getEmail());
		updatecl.setString(7, cliente.getCodCl());
		int row = updatecl.executeUpdate();
		if (row < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getIdCt(String codfisc) throws SQLException {
		ResultSet rs = idcl.executeQuery("SELECT codcliente FROM cliente WHERE codicefiscale = '" + codfisc + "'");
		if (rs.next()) {
			id = rs.getString("codcliente");
		}
		rs.close();
		return id;
	}

	@Override
	public Cliente getCtByid(String idct) throws SQLException {
		ResultSet rs = idcl.executeQuery("SELECT * FROM cliente WHERE codcliente = '" + idct + "'");
		if (rs.next()) {
			ct = new Cliente(null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null, null);
		}
		rs.close();
		return ct;
	}
}
