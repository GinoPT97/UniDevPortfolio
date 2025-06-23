package daointerface;

import java.sql.SQLException;
import java.util.List;

import model.Articoli;
import model.Cliente;

public interface ArticoliJDBC {
    public boolean newordine(Articoli articoli) throws SQLException;

    public List<Cliente> searchClient() throws SQLException;
}
