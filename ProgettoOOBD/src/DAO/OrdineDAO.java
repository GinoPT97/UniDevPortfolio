package DAO;

import DBEntities.Cliente;
import DBEntities.Ordine;
import java.util.ArrayList;

public interface OrdineDAO {

    //Input:
    public boolean createOrdine(Ordine ordine);

    //Output
    public ArrayList<Ordine> getAllOrdini();
    public ArrayList<Ordine> getOrdiniListByCliente(Cliente cliente);
    public ArrayList<Cliente> getClienteListByCategoriaProdotti(String categoria);

}
