package JDBCimpl;

import java.util.ArrayList;

import Entita.Prodotto;
import JDBC.ProdottoJDBC;

import java.sql.*;

public class Prodottoimpl implements ProdottoJDBC{
	private Connection connection;
	private PreparedStatement setNewProdotto, getallprodotti, updateprodotto, upscorte;
	private Statement getcategoria;
	private ArrayList<Prodotto> prod = new ArrayList<Prodotto>();
	private ArrayList<Prodotto> prodc = new ArrayList<Prodotto>();
	
	public Prodottoimpl(Connection connection) throws SQLException{
		this.connection = connection;
		getallprodotti = connection.prepareStatement("SELECT * FROM prodotto ORDER BY nome DESC");
		setNewProdotto = connection.prepareStatement("INSERT INTO prodotto VALUES (NEXTVAL('SCodProdotto'), ?, ?, ?, ?, ?, ?, CAST(? AS BOOLEAN), ?, CAST(? AS TIPOLOGIA), ?)");
		updateprodotto = connection.prepareStatement("UPDATE prodotto SET nome=?, descrizione=?, prezzo=?, luogoprovenienza=?, dataraccolta=?, datamungitura=?, glutine=CAST(? AS BOOLEAN), datascadenza=?, categoria=CAST(? AS TIPOLOGIA), scorta=? WHERE codprodotto = ?");
		upscorte = connection.prepareStatement("UPDATE prodotto SET scorta = scorta - ? WHERE codprodotto = ?");
		getcategoria = connection.createStatement();
	}
	
	@Override
	public boolean setNewProdotto(Prodotto prodotto) throws SQLException{
		setNewProdotto.setString(1, prodotto.getNome());
		setNewProdotto.setString(2, prodotto.getDscrizione());
		setNewProdotto.setDouble(3, prodotto.getPrezzo());
		setNewProdotto.setString(4, prodotto.getLuogoProv());
		if(prodotto.getCategoria().equals("Ortofrutticoli")) {
			setNewProdotto.setDate(5, new java.sql.Date(prodotto.getDataracc().getTime()));
			setNewProdotto.setDate(6, null);
			setNewProdotto.setBoolean(7, prodotto.getGlutine());
			setNewProdotto.setDate(8, null);
			setNewProdotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Inscatolati")) {
			setNewProdotto.setDate(5, null);
			setNewProdotto.setDate(6, null);
			setNewProdotto.setBoolean(7, prodotto.getGlutine());
			setNewProdotto.setDate(8, new java.sql.Date(prodotto.getDatascad().getTime()));
			setNewProdotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Latticini")) {
			setNewProdotto.setDate(5, null);
			setNewProdotto.setDate(6, new java.sql.Date(prodotto.getDatamung().getTime()));
			setNewProdotto.setBoolean(7, prodotto.getGlutine());
			setNewProdotto.setDate(8, null);
			setNewProdotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Farinacei")) {
			setNewProdotto.setDate(5, null);
			setNewProdotto.setDate(6, null);
			setNewProdotto.setBoolean(7, prodotto.getGlutine());
			setNewProdotto.setDate(8, null);
			setNewProdotto.setString(9, prodotto.getCategoria());
		}
		setNewProdotto.setInt(10, prodotto.getScorta());
        int row = setNewProdotto.executeUpdate();
        if(row<1) return false;
        else return true;
	}

	@Override
	public ArrayList<Prodotto> getallprodotti() throws SQLException {
		prod.clear();
		ResultSet rs = getallprodotti.executeQuery();
        while(rs.next()) prod.add(new Prodotto(rs.getString("codprodotto"),rs.getString("nome"),
        			                    rs.getString("descrizione"),rs.getDouble("prezzo"), 
        			                    rs.getString("luogoprovenienza"),
        			                    rs.getDate("dataraccolta"), rs.getDate("datamungitura"),
        			                    rs.getBoolean("glutine"), rs.getDate("datascadenza"),
        			                    rs.getString("categoria"),rs.getInt("scorta")));
        rs.close();
        return prod;
	}

	@Override
	public boolean updateProdotto(Prodotto prodotto) throws SQLException {
		updateprodotto.setString(1, prodotto.getNome());
		updateprodotto.setString(2, prodotto.getDscrizione());
		updateprodotto.setDouble(3, prodotto.getPrezzo());
		updateprodotto.setString(4, prodotto.getLuogoProv());
		if(prodotto.getCategoria().equals("Ortofrutticoli")) {
			updateprodotto.setDate(5, new java.sql.Date(prodotto.getDataracc().getTime()));
			updateprodotto.setDate(6, null);
			updateprodotto.setBoolean(7, prodotto.getGlutine());
			updateprodotto.setDate(8, null);
			updateprodotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Inscatolati")) {
			updateprodotto.setDate(5, null);
			updateprodotto.setDate(6, null);
			updateprodotto.setBoolean(7, prodotto.getGlutine());
			updateprodotto.setDate(8, new java.sql.Date(prodotto.getDatascad().getTime()));
			updateprodotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Latticini")) {
			updateprodotto.setDate(5, null);
			updateprodotto.setDate(6, new java.sql.Date(prodotto.getDatamung().getTime()));
			updateprodotto.setBoolean(7, prodotto.getGlutine());
			updateprodotto.setDate(8, null);
			updateprodotto.setString(9, prodotto.getCategoria());
		} else if(prodotto.getCategoria().equals("Farinacei")) {
			updateprodotto.setDate(5, null);
			updateprodotto.setDate(6, null);
			updateprodotto.setBoolean(7, prodotto.getGlutine());
			updateprodotto.setDate(8, null);
			updateprodotto.setString(9, prodotto.getCategoria());
		}
		updateprodotto.setInt(10, prodotto.getScorta());
		updateprodotto.setString(11, prodotto.getCodProd());
		int row = updateprodotto.executeUpdate();
		if(row<1) return false;
        else return true;
	}

	@Override
	public ArrayList<Prodotto> getbycategoria(String categoria) throws SQLException {
		prodc.clear();
		ResultSet rs = getcategoria.executeQuery("SELECT * FROM prodotto WHERE categoria = '"+categoria+"' ORDER BY nome DESC");
		while( rs.next()) prodc.add(new Prodotto( rs.getString("codprodotto"), rs.getString("nome"),
        			                     rs.getString("descrizione"), rs.getDouble("prezzo"), 
        			                     rs.getString("luogoprovenienza"), rs.getDate("dataraccolta"), 
        			                     rs.getDate("datamungitura"), rs.getBoolean("glutine"), 
        			                     rs.getDate("datascadenza"),  rs.getString("categoria"),
        			                     rs.getInt("scorta")));
         rs.close();
		return prodc;
	}

	@Override
	public boolean updateScorte(int x, String codprod) throws SQLException {
		upscorte.setInt(1, x);
		upscorte.setString(2, codprod);
		int row = upscorte.executeUpdate();
		if(row<1) return false;
        else return true;
	}
}
