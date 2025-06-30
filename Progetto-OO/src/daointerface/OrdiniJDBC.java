package daointerface;

import model.Ordine;

import java.sql.SQLException;
import java.util.List;

public interface OrdiniJDBC {
    public boolean newordine(Ordine ordine) throws SQLException;

    public List<Ordine> getallordini() throws SQLException;

    public String getOldDate() throws SQLException;

    public String getCurrentCod() throws SQLException;
}
