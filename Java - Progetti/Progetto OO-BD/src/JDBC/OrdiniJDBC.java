package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Ordine;

public interface OrdiniJDBC {
	public boolean newordine(Ordine ordine) throws SQLException;

	public ArrayList<Ordine> getallordini() throws SQLException;

	public String getOldDate() throws SQLException;

	public String getCurrentCod() throws SQLException;
}
