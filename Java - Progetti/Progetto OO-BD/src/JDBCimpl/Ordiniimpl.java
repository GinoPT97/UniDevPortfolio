package JDBCimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import Entita.Ordine;
import JDBC.OrdiniJDBC;

public class Ordiniimpl implements OrdiniJDBC{
	private Connection connection;
	private PreparedStatement newordine,allordine,newarticoli;
	private Statement olddate,currCod;
	private ArrayList<Ordine> ord = new ArrayList<>();

	public Ordiniimpl(Connection connection) throws SQLException{
		this.connection = connection;
		newordine = connection.prepareStatement("INSERT INTO ordine VALUES (NEXTVAL('SCodOrdine'),?,?,?,?)");
		newarticoli = connection.prepareStatement("INSERT INTO articoliordine VALUES (NEXTVAL(?,?,?,?)");
		allordine = connection.prepareStatement("SELECT * FROM ordine ORDER BY dataacquisto DESC");
		olddate = connection.createStatement();
		currCod = connection.createStatement();
	}

	@Override
	public boolean newordine(Ordine ordine) throws SQLException{
		newordine.setDouble(1, ordine.getPrezzoT());
		newordine.setDate(2, ordine.getAcquisto());
		newordine.setString(3, ordine.getIdCt());
		newordine.setString(4, ordine.getIdDip());
        int row = newordine.executeUpdate();
        if(row<1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public ArrayList<Ordine> getallordini() throws SQLException {
		ResultSet rs = allordine.executeQuery();
		while(rs.next()) {
			ord.add(new Ordine(rs.getString("codordine"), rs.getDate("dataacquisto"),
					                                  rs.getDouble("prezzototale"), rs.getString("codcliente"),
					                                  rs.getString("coddipendente")));
		}
		rs.close();
		return ord;
	}

	@Override
	public String getOldDate() {
	    String query = "SELECT MIN(dataacquisto) AS old FROM ordine";
	    try (PreparedStatement ps = connection.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            Date oldDate = rs.getDate("old");
	            if (oldDate != null) {
	                return oldDate.toString();
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error executing query: " + e.getMessage());
	    }
	    return null;
	}

	@Override
	public String getCurrentCod() throws SQLException {
		ResultSet rs = currCod.executeQuery("SELECT currval('SCodOrdine') AS codordine");
		String codOrd = null;
		if(rs.next()) {
			codOrd = rs.getString("codordine");
		}
		rs.close();
		return codOrd;
	}
}