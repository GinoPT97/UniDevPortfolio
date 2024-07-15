package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Articoli;
import Model.Cliente;

public interface ArticoliJDBC {
	public boolean newordine(Articoli articoli) throws SQLException;
    public ArrayList<Cliente> SearchClient() throws SQLException;
}
