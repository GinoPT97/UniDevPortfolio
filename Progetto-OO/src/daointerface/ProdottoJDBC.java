package daointerface;

import model.Prodotto;

import java.sql.SQLException;
import java.util.List;

public interface ProdottoJDBC {
    public boolean setNewProdotto(Prodotto prodotto) throws SQLException;

    public boolean updateProdotto(Prodotto prodotto) throws SQLException;

    public boolean updateScorte(int x, String codprod) throws SQLException;

    public List<Prodotto> getallprodotti() throws SQLException;

    public List<Prodotto> getbycategoria(String categoria) throws SQLException;
}
