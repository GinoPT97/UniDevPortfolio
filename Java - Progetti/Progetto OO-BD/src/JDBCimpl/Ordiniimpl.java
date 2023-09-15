package JDBCimpl;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import Entita.Cliente;
import Entita.Dipendente;
import Entita.Ordine;
import Entita.Tessera;
import JDBC.OrdiniJDBC;

public class Ordiniimpl implements OrdiniJDBC{
	private Connection connection;
	private PreparedStatement newordine,allordine,newarticoli;
	private Statement olddate,currCod;
	private ArrayList<Ordine> ord = new ArrayList<Ordine>();
	
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
        if(row<1) return false;
        else return true;
	}

	@Override
	public ArrayList<Ordine> getallordini() throws SQLException {
		ResultSet rs = allordine.executeQuery();
		while(rs.next()) ord.add(new Ordine(rs.getString("codordine"), rs.getDate("dataacquisto"), 
				                                  rs.getDouble("prezzototale"), rs.getString("codcliente"), 
				                                  rs.getString("coddipendente")));
		rs.close();
		return ord;
	}

	@Override
	public String getOldDate() throws SQLException {
		ResultSet rs = olddate.executeQuery("SELECT MIN(dataacquisto) AS old FROM ordine");
		String old = null;
		if(rs.next()) old = rs.getDate("old").toString();
		rs.close();
		return old;
	}

	@Override
	public String getCurrentCod() throws SQLException {
		ResultSet rs = currCod.executeQuery("SELECT currval('SCodOrdine') AS codordine");
		String codOrd = null;
		if(rs.next()) codOrd = rs.getString("codordine");
		rs.close();
		return codOrd;
	}
}