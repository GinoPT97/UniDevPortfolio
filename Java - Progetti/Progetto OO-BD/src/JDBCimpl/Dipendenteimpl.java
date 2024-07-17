package JDBCimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import JDBC.DipendenteJDBC;
import Model.Dipendente;

public class Dipendenteimpl implements DipendenteJDBC {
	private PreparedStatement setNewDip, getAllDip, updatedp;
	private Statement getDipVendite, getDipIntroiti, verifyId, getdip, lastsell;
	private ArrayList<Dipendente> diplist = new ArrayList<>();
	private List<String> ordven = new ArrayList<>();
	private List<String> ordint = new ArrayList<>();
	private Dipendente dip;

	public Dipendenteimpl(Connection connection) throws SQLException {
		getAllDip = connection.prepareStatement("SELECT * FROM dipendente ORDER BY cognome DESC");
		setNewDip = connection
				.prepareStatement("INSERT INTO dipendente VALUES (NEXTVAL('SCodDipendente'), ?, ?, ?, ?, ?, ?)");
		updatedp = connection.prepareStatement(
				"UPDATE dipendente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE coddipendente = ?");
		getdip = connection.createStatement();
		verifyId = connection.createStatement();
		getDipVendite = connection.createStatement();
		getDipIntroiti = connection.createStatement();
	}

	@Override
	public boolean setNewDip(Dipendente dipendente) throws SQLException {
		setNewDip.setString(1, dipendente.getNome());
		setNewDip.setString(2, dipendente.getCognome());
		setNewDip.setString(3, dipendente.getCodFis());
		setNewDip.setString(4, dipendente.getInd());
		setNewDip.setString(5, dipendente.getTel());
		setNewDip.setString(6, dipendente.getEmail());
		int row = setNewDip.executeUpdate();
		if (row < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean verifyID(String id) throws SQLException {
		ResultSet rs = verifyId
				.executeQuery("SELECT coddipendente FROM dipendente WHERE coddipendente = '" + id + "';");
		if (rs.next()) {
			if (rs.getString("coddipendente").equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<Dipendente> getAllDip() throws SQLException {
		ResultSet rs = getAllDip.executeQuery();
		while (rs.next()) {
			diplist.add(new Dipendente(rs.getString("coddipendente"), rs.getString("nome"), rs.getString("cognome"),
					rs.getString("codicefiscale"), rs.getString("email"), rs.getString("indirizzo"),
					rs.getString("telefono")));
		}
		rs.close();
		return diplist;
	}

	@Override
	public List<String> getDipVendite(Date di, Date df) throws SQLException {
		ordven.clear();
		ResultSet rs = getDipVendite.executeQuery("SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini\n"
				+ "FROM dipendente AS D, ordine AS O\n"
				+ "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto >= '" + di + "' AND O.dataacquisto <= '"
				+ df + "'\n" + "GROUP BY D.nome,D.cognome\n" + "ORDER BY Tordini DESC\n" + "LIMIT 1");
		while (rs.next()) {
			ordven.add(rs.getString("nome"));
			ordven.add(rs.getString("cognome"));
			ordven.add(rs.getString("Tordini") + "");
		}
		rs.close();
		return ordven;
	}

	@Override
	public List<String> getDipIntroiti(Date di, Date df) throws SQLException {
		ordint.clear();
		ResultSet rs = getDipIntroiti.executeQuery("SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine\n"
				+ "FROM dipendente AS D, ordine AS O \n"
				+ "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto >= '" + di + "' AND O.dataacquisto <= '"
				+ df + "'\n" + "GROUP BY D.nome,D.cognome\n" + "ORDER BY Sordine DESC\n" + "LIMIT 1");
		while (rs.next()) {
			ordint.add(rs.getString("nome"));
			ordint.add(rs.getString("cognome"));
			ordint.add(rs.getString("Sordine") + "");
		}
		rs.close();
		return ordint;
	}

	@Override
	public boolean updatedipendente(Dipendente dipendente) throws SQLException {
		updatedp.setString(1, dipendente.getNome());
		updatedp.setString(2, dipendente.getCognome());
		updatedp.setString(3, dipendente.getCodFis());
		updatedp.setString(4, dipendente.getInd());
		updatedp.setString(5, dipendente.getTel());
		updatedp.setString(6, dipendente.getEmail());
		updatedp.setString(7, dipendente.getCodDIP());
		int row = updatedp.executeUpdate();
		if (row < 1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Dipendente getOneDip(String id) throws SQLException {
		ResultSet rs = getdip.executeQuery("SELECT * FROM dipendente WHERE coddipendente = '" + id + "'");
		while (rs.next()) {
			dip = new Dipendente(id, rs.getString("nome"), rs.getString("cognome"), rs.getString("codicefiscale"),
					rs.getString("email"), rs.getString("indirizzo"), rs.getString("telefono"));
		}
		rs.close();
		return dip;
	}
}
