package DAO;

import DBEntities.Prodotto;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ProdottoDAO {

    //Input:
    public boolean insertProdotto(Prodotto prodotto) throws SQLException;
    //public int autodeleteExpiredInscatolati();


    //Output:
    public ArrayList<Prodotto> getAllProdotti();
    public ArrayList<Prodotto> getProdottiListByCategoria(String categoria);
    public ArrayList<Prodotto> getProdottiListByLuogoProvenienza(String luogoProvenienza);
    public ArrayList<Prodotto> getProdottiListByPrezzo(float prezzo);
    public int checkScortaOfProdotto(Prodotto prodotto);
    public boolean updateProdotto(String id, String name, String description, float price,
                                  String place, java.util.Date collectionDate, java.util.Date milkingDate, Boolean gluten,
                                  java.util.Date expirationDate, String cateogory, int stock) throws SQLException;


}
