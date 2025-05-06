package daointerface;

import java.sql.SQLException;
import java.util.List;

import Model.Articoli;
import Model.Cliente;

public interface ArticoliJDBC {
	public boolean newordine(Articoli articoli) throws SQLException;

	public List<Cliente> searchClient() throws SQLException;
}
