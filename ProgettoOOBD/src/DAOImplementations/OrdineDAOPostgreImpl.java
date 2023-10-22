package DAOImplementations;

import DAO.OrdineDAO;
import DBEntities.Cliente;
import DBEntities.Ordine;

import java.sql.Connection;
import java.util.ArrayList;

public class OrdineDAOPostgreImpl implements OrdineDAO {

    public OrdineDAOPostgreImpl(Connection connection) {

    }

    @Override
    public boolean createOrdine(Ordine ordine) {
        return false;
    }

    @Override
    public ArrayList<Ordine> getAllOrdini() {
        return null;
    }

    @Override
    public ArrayList<Ordine> getOrdiniListByCliente(Cliente cliente) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClienteListByCategoriaProdotti(String categoria) {
        return null;
    }
}
