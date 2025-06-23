package daointerface;

import java.sql.SQLException;
import java.util.List;

import model.Cliente;

public interface ClienteJDBC {
    public boolean setNewCt(Cliente cliente) throws SQLException;

    public boolean updateCliente(Cliente cliente) throws SQLException;

    public List<Cliente> getAllCt() throws SQLException;

    public String getIdCt(String codfisc) throws SQLException;

    public String getCtByNCCF(String name, String cognome, String codicefiscale) throws SQLException;

    public Cliente getCtByid(String idct) throws SQLException;
}
