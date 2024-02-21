package JDBCimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Entita.Cliente;
import Entita.Tessera;
import JDBC.TesseraJDBC;

public class Tesseraimpl implements TesseraJDBC{
	private Connection connection;
	private PreparedStatement newtessera, getpuntit, alltessera, uppunti;
	private ArrayList<Tessera> tess = new ArrayList<>();
	private String s;

	public Tesseraimpl(Connection connection) throws SQLException{
		this.connection = connection;
		newtessera = connection.prepareStatement("INSERT INTO tessera VALUES (NEXTVAL('SCodTessera'),?,?)");
		getpuntit = connection.prepareStatement("SELECT numeropunti FROM tessera WHERE codtessera = ?");
		alltessera = connection.prepareStatement("SELECT * FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente  ORDER BY C.cognome DESC");
		uppunti = connection.prepareStatement("UPDATE tessera SET numeropunti = numeropunti + ? WHERE codcliente = ?");
	}

	@Override
	public boolean newtessera(String codcl) throws SQLException{
		newtessera.setDouble(1, 0.00);
		newtessera.setString(2, codcl);
        int row = newtessera.executeUpdate();
		if(row<1) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getpuntit(String codt) throws SQLException {
		    getpuntit.setString(1, codt);
		    ResultSet rs = getpuntit.executeQuery();
	        while(rs.next()) {
				s = rs.getDouble("numeropunti") + "";
			}
	        rs.close();
	        return s;
	}

	@Override
	public ArrayList<Tessera> alltessera() throws SQLException {
		ResultSet rs = alltessera.executeQuery();
        while(rs.next()) {
			tess.add(new Tessera(rs.getString("codtessera"),rs.getInt("numeropunti"),
					                  new Cliente(null,rs.getString("nome"),
			                          rs.getString("cognome"),rs.getString("codicefiscale"),
			                          rs.getString("email"),rs.getString("indirizzo"),
			                          rs.getString("telefono"), null,null)));
		}
        rs.close();
        return tess;
	}

	@Override
	public boolean updatepunti(String codcl,double x) throws SQLException {
		double p = (x*10)/100;
		uppunti.setFloat(1, (float) p);
		uppunti.setString(2, codcl);
		int row = uppunti.executeUpdate();
		if(row<1) {
			return false;
		} else {
			return true;
		}
	}
}

