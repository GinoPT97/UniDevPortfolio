package daointerface;

import java.sql.SQLException;
import java.util.List;

import model.Tessera;

public interface TesseraJDBC {
	public boolean newtessera(String cliente) throws SQLException;

	public List<Tessera> alltessera() throws SQLException;

	public String getpuntit(String codt) throws SQLException;

	public boolean updatepunti(String codcl, double d) throws SQLException;

}
