package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;

import Entita.Prodotto;

public interface ProdottoJDBC {
        public boolean setNewProdotto(Prodotto prodotto) throws SQLException;
        public boolean updateProdotto(Prodotto prodotto) throws SQLException;
        public boolean updateScorte(int x, String codprod) throws SQLException;
        public ArrayList<Prodotto> getallprodotti() throws SQLException;
        public ArrayList<Prodotto> getbycategoria(String categoria) throws SQLException;
}
