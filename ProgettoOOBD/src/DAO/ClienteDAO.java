package DAO;

import DBEntities.Cliente;
import java.util.ArrayList;

public interface ClienteDAO {

    //Input:
    public boolean insertCliente(Cliente cliente);


    //Output:
    public ArrayList<Cliente> getAllClienti();
    public ArrayList<Cliente> getClientiListByCognome(String cognome);
    public ArrayList<Cliente> getClientiListByNomeCognome(String nome, String cognome);
    public ArrayList<Cliente> getClientiListByComune(String comune);
    public ArrayList<Cliente> getClientiListByAnnoNascita(String anno);
    public ArrayList<Cliente> getClientiListByNumeroPunti();
    public ArrayList<Cliente> getClientiListByPuntiPerCategoria(String categoria);
    public ArrayList<Cliente> getClientiListByCategoriaProdotti(String categoria);

}
