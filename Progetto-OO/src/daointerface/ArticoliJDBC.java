package daointerface;

import model.Articoli;
import model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ArticoliJDBC {
    public boolean newordine(Articoli articoli) throws SQLException;

    public List<Cliente> searchClient() throws SQLException;
}
