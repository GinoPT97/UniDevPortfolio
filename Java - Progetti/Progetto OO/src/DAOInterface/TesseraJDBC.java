package DAOInterface;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Tessera;

public interface TesseraJDBC {
	public boolean newtessera(String cliente) throws SQLException;

	public ArrayList<Tessera> alltessera() throws SQLException;

	public String getpuntit(String codt) throws SQLException;

	public boolean updatepunti(String codcl, double d) throws SQLException;
}
