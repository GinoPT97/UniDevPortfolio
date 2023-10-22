package DAOImplementations;

import DAO.TesseraDAO;
import DBEntities.Cliente;
import DBEntities.Tessera;

import java.sql.Connection;
import java.util.ArrayList;

public class TesseraDAOPostgreImpl implements TesseraDAO {


    public TesseraDAOPostgreImpl(Connection connection) {

    }

    @Override
    public boolean createTessera(Tessera tessera) {
        return false;
    }

    /*@Override
    public int autodeleteExpiredTessere() {
        return -1;
    }
     */
}
