package DAOImplementations;

import DAO.ClienteDAO;
import DBEntities.Cliente;

import java.sql.Connection;
import java.util.ArrayList;

public class ClienteDAOPostgreImpl implements ClienteDAO {

    public ClienteDAOPostgreImpl(Connection connection) {

    }

    @Override
    public boolean insertCliente(Cliente cliente) {
        return false;
    }

    @Override
    public ArrayList<Cliente> getAllClienti() {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByComune(String comune) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByAnnoNascita(String anno) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByCognome(String cognome) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByNomeCognome(String nome, String cognome) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByNumeroPunti() {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByPuntiPerCategoria(String categoria) {
        return null;
    }

    @Override
    public ArrayList<Cliente> getClientiListByCategoriaProdotti(String categoria) {
        return null;
    }
}
