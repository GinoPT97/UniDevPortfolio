package DAO;

import DBEntities.Cliente;
import DBEntities.Tessera;

import java.util.ArrayList;

public interface TesseraDAO {

    //Input:
    public boolean createTessera(Tessera tessera);
    //public int autodeleteExpiredTessere();

}
