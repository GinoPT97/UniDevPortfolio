package DAOImplementations;

import DAO.ProdottoDAO;
import DBEntities.Dipendente;
import DBEntities.Prodotto;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProdottoDAOPostgreImpl implements ProdottoDAO {

    private ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
    private Connection connection;
    private Statement getProdotti;
    private Statement filterProdotti;
    private PreparedStatement insertProdotto;
    private PreparedStatement updateProdotto;

    public ProdottoDAOPostgreImpl(Connection connection) throws SQLException {
        this.connection = connection;
        getProdotti = connection.createStatement();
        filterProdotti = connection.createStatement();
        insertProdotto = connection.prepareStatement("INSERT INTO PRODOTTO VALUES (NEXTVAL('SqCodProdotto'), ?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS Tipologia), ?);"); //Per il tipo 'custom' TIPOLOGIA (ENUM) è stato necessario effettuare una conversione dal tipo VARCHAR
        updateProdotto = connection.prepareStatement("UPDATE PRODOTTO SET Nome = ?,\n" +
                "Descrizione = ?,\n" +
                "Prezzo = ?,\n" +
                "LuogoProvenienza = ?,\n" +
                "DataRaccolta = ?,\n" +
                "DataMungitura = ?,\n" +
                "Glutine = ?,\n" +
                "DataScadenza = ?,\n" +
                "Categoria = CAST(? AS Tipologia),\n" +
                "Scorta = ?\n" +
                "WHERE CodProdotto = ?;");
    }

    @Override
    public boolean insertProdotto(Prodotto prodotto) throws SQLException {
        insertProdotto.setString(1, prodotto.getNome());
        if (prodotto.getDescrizione().equals(""))
            insertProdotto.setString(2, null);
        else
            insertProdotto.setString(2, prodotto.getDescrizione());
        insertProdotto.setFloat(3, prodotto.getPrezzo());
        if (prodotto.getLuogoProvenienza().equals(""))
            insertProdotto.setString(4, null);
        else
            insertProdotto.setString(4, prodotto.getLuogoProvenienza());
        if (prodotto.getCategoria().equals("Ortofrutticoli")) {
            insertProdotto.setDate(5, new java.sql.Date(prodotto.getDataRaccolta().getTime())); //Il parametro dichiarato nella classe 'Prodotto' è di tipo java.util.Date e deve essere convertito in java.sql.Date
            insertProdotto.setDate(6, null);
            insertProdotto.setNull(7, Types.NULL); //Un tipo boolean non può essere null, dunque non è stato possibile utilizzare il metodo setBoolean;
            insertProdotto.setDate(8, null);
        }
        if (prodotto.getCategoria().equals("Latticini")) {
            insertProdotto.setDate(5, null);
            insertProdotto.setDate(6, new java.sql.Date(prodotto.getDataMungitura().getTime()));
            insertProdotto.setNull(7, Types.NULL);
            insertProdotto.setDate(8, null);
        }
        if (prodotto.getCategoria().equals("Farinacei")) {
            insertProdotto.setDate(5, null);
            insertProdotto.setDate(6, null);
            insertProdotto.setBoolean(7, prodotto.getGlutine());
            insertProdotto.setDate(8, null);
        }
        if (prodotto.getCategoria().equals("Inscatolati")) {
            insertProdotto.setDate(5, null);
            insertProdotto.setDate(6, null);
            insertProdotto.setNull(7, Types.NULL);
            insertProdotto.setDate(8, new java.sql.Date(prodotto.getDataScadenza().getTime()));
        }
        insertProdotto.setString(9, prodotto.getCategoria());
        // Un approccio alternativo per il tipo custom 'TIPOLOGIA' sarebbe stato insertProdotto.setObject(9, prodotto.getCategoria(), Types.OTHER); fonte: https://stackoverflow.com/questions/10571821/inserting-into-custom-sql-types-with-prepared-statements-in-java
        insertProdotto.setInt(10, prodotto.getScorta());
        int rows = insertProdotto.executeUpdate();
        if (rows < 1)
            return false;
        else
            return true;
    }

    @Override
    public ArrayList<Prodotto> getAllProdotti() {
        prodotti.clear();
        try {
            ResultSet rs = getProdotti.executeQuery("SELECT * FROM PRODOTTO");
            while (rs.next()) {
                prodotti.add(new Prodotto(rs.getString("CodProdotto"), rs.getString("Nome"),
                        rs.getString("Descrizione"), rs.getBigDecimal("Prezzo").floatValue(),
                        rs.getString("LuogoProvenienza"), rs.getDate("DataRaccolta"),
                        rs.getDate("DataMungitura"), rs.getDate("DataScadenza"),
                        (Boolean)rs.getObject("Glutine"), rs.getString("Categoria"),
                        rs.getInt("Scorta")));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
        }
        return prodotti;
    }

    @Override
    public ArrayList<Prodotto> getProdottiListByCategoria(String categoria) {
        return null;
    }

    @Override
    public ArrayList<Prodotto> getProdottiListByLuogoProvenienza(String luogoProvenienza) {
        return null;
    }

    @Override
    public ArrayList<Prodotto> getProdottiListByPrezzo(float prezzo) {
        return null;
    }

    @Override
    public int checkScortaOfProdotto(Prodotto prodotto) {
        return 0;
    }

    @Override
    public boolean updateProdotto(String id, String name, String description, float price, String place, java.util.Date collectionDate, java.util.Date milkingDate, Boolean gluten, java.util.Date expirationDate, String cateogory, int stock) throws SQLException {
        updateProdotto.setString(1, name);
        updateProdotto.setString(2, description);
        updateProdotto.setFloat(3, price);
        updateProdotto.setString(4, place);
        if (cateogory.equals("Ortofrutticoli")) {
            updateProdotto.setDate(5, new java.sql.Date(collectionDate.getTime()));
            updateProdotto.setDate(6, null);
            updateProdotto.setNull(7, Types.NULL);
            updateProdotto.setDate(8, null);
        }
        if (cateogory.equals("Latticini")) {
            updateProdotto.setDate(5, null);
            updateProdotto.setDate(6, new java.sql.Date(milkingDate.getTime()));
            updateProdotto.setNull(7, Types.NULL);
            updateProdotto.setDate(8, null);
        }
        if (cateogory.equals("Farinacei")) {
            updateProdotto.setDate(5, null);
            updateProdotto.setDate(6, null);
            updateProdotto.setBoolean(7, gluten);
            updateProdotto.setDate(8, null);
        }
        if (cateogory.equals("Inscatolati")) {
            updateProdotto.setDate(5, null);
            updateProdotto.setDate(6, null);
            updateProdotto.setNull(7, Types.NULL);
            updateProdotto.setDate(8, (Date) expirationDate);
        }
        updateProdotto.setString(9, cateogory);
        updateProdotto.setInt(10, stock);
        updateProdotto.setString(11, id);
        int rows = updateProdotto.executeUpdate();
        if (rows < 1)
            return false;
        else
            return true;
    }

    public ArrayList<Prodotto> filterProdottiListBy(String filter) {
        prodotti.clear();
        if (filter.equals("Codice Prodotto"))
            filter = "CodProdotto";
        try {
            ResultSet rs = filterProdotti.executeQuery("SELECT * FROM PRODOTTO ORDER BY " + filter + " ASC");
            while (rs.next()) {
                prodotti.add(new Prodotto(rs.getString("CodProdotto"), rs.getString("Nome"),
                        rs.getString("Descrizione"), rs.getBigDecimal("Prezzo").floatValue(),
                        rs.getString("LuogoProvenienza"), rs.getDate("DataRaccolta"),
                        rs.getDate("DataMungitura"), rs.getDate("DataScadenza"),
                        (Boolean)rs.getObject("Glutine"), rs.getString("Categoria"),
                        rs.getInt("Scorta")));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
        }
        return prodotti;
    }
}
