package daointerface;

import java.sql.SQLException;
import java.util.List;

import model.Ordine;

public interface OrdiniJDBC {
	public boolean newordine(Ordine ordine) throws SQLException;

	public List<Ordine> getallordini() throws SQLException;

	public String getOldDate() throws SQLException;

	public String getCurrentCod() throws SQLException;
}
