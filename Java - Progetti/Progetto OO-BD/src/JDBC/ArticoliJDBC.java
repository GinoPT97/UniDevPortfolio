package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;

import Entita.Articoli;
import Entita.Cliente;

public interface ArticoliJDBC {
	public boolean newordine(Articoli articoli) throws SQLException;
    public ArrayList<Cliente> SearchClient() throws SQLException;
}
