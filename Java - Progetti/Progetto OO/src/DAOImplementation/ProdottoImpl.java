package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DAOInterface.ProdottoJDBC;
import Model.Prodotto;

public class ProdottoImpl implements ProdottoJDBC {
    private PreparedStatement setNewProdottoStmt, getAllProdottiStmt, updateProdottoStmt, updateScorteStmt;
    private Statement getCategoriaStmt;

    // Costruttore che inizializza la connessione e le query preparate
    public ProdottoImpl(Connection connection) throws SQLException {
        this.getAllProdottiStmt = connection.prepareStatement("SELECT * FROM prodotto ORDER BY nome DESC");
        this.setNewProdottoStmt = connection.prepareStatement(
            "INSERT INTO prodotto VALUES (NEXTVAL('SCodProdotto'), ?, ?, ?, ?, ?, ?, CAST(? AS BOOLEAN), ?, CAST(? AS TIPOLOGIA), ?)"
        );
        this.updateProdottoStmt = connection.prepareStatement(
            "UPDATE prodotto SET nome=?, descrizione=?, prezzo=?, luogoprovenienza=?, dataraccolta=?, datamungitura=?, glutine=CAST(? AS BOOLEAN), datascadenza=?, categoria=CAST(? AS TIPOLOGIA), scorta=? WHERE codprodotto = ?"
        );
        this.updateScorteStmt = connection.prepareStatement("UPDATE prodotto SET scorta = scorta - ? WHERE codprodotto = ?");
        this.getCategoriaStmt = connection.createStatement();
    }

    // Metodo per inserire un nuovo prodotto
    @Override
    public boolean setNewProdotto(Prodotto prodotto) throws SQLException {
        setCommonProdottoFields(setNewProdottoStmt, prodotto);
        setNewProdottoStmt.setInt(10, prodotto.getScorta());

        return setNewProdottoStmt.executeUpdate() > 0;
    }

    // Metodo per aggiornare un prodotto esistente
    @Override
    public boolean updateProdotto(Prodotto prodotto) throws SQLException {
        setCommonProdottoFields(updateProdottoStmt, prodotto);
        updateProdottoStmt.setInt(10, prodotto.getScorta());
        updateProdottoStmt.setString(11, prodotto.getCodProd());

        return updateProdottoStmt.executeUpdate() > 0;
    }

    // Metodo per aggiornare le scorte di un prodotto
    @Override
    public boolean updateScorte(int x, String codprod) throws SQLException {
        updateScorteStmt.setInt(1, x);
        updateScorteStmt.setString(2, codprod);

        return updateScorteStmt.executeUpdate() > 0;
    }

    // Metodo per ottenere tutti i prodotti
    @Override
    public ArrayList<Prodotto> getallprodotti() throws SQLException {
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try (ResultSet rs = getAllProdottiStmt.executeQuery()) {
            while (rs.next()) {
                prodotti.add(createProdottoFromResultSet(rs));
            }
        }
        return prodotti;
    }

    // Metodo per ottenere i prodotti filtrati per categoria
    @Override
    public ArrayList<Prodotto> getbycategoria(String categoria) throws SQLException {
        ArrayList<Prodotto> prodottiCategoria = new ArrayList<>();
        try (ResultSet rs = getCategoriaStmt.executeQuery("SELECT * FROM prodotto WHERE categoria = '" + categoria + "' ORDER BY nome DESC")) {
            while (rs.next()) {
                prodottiCategoria.add(createProdottoFromResultSet(rs));
            }
        }
        return prodottiCategoria;
    }

    // Metodo ausiliario per impostare i campi comuni dei prodotti in base alla categoria
    private void setCommonProdottoFields(PreparedStatement stmt, Prodotto prodotto) throws SQLException {
        stmt.setString(1, prodotto.getNome());
        stmt.setString(2, prodotto.getDescrizione());
        stmt.setDouble(3, prodotto.getPrezzo());
        stmt.setString(4, prodotto.getLuogoProv());

        switch (prodotto.getCategoria()) {
            case "Ortofrutticoli":
                stmt.setDate(5, new java.sql.Date(prodotto.getDataraccolta().getTime()));
                stmt.setDate(6, null);
                stmt.setBoolean(7, prodotto.isGlutine());
                stmt.setDate(8, null);
                break;
            case "Inscatolati":
                stmt.setDate(5, null);
                stmt.setDate(6, null);
                stmt.setBoolean(7, prodotto.isGlutine());
                stmt.setDate(8, new java.sql.Date(prodotto.getDatascadenza().getTime()));
                break;
            case "Latticini":
                stmt.setDate(5, null);
                stmt.setDate(6, new java.sql.Date(prodotto.getDatamungitura().getTime()));
                stmt.setBoolean(7, prodotto.isGlutine());
                stmt.setDate(8, null);
                break;
            case "Farinacei":
                stmt.setDate(5, null);
                stmt.setDate(6, null);
                stmt.setBoolean(7, prodotto.isGlutine());
                stmt.setDate(8, null);
                break;
        }
        stmt.setString(9, prodotto.getCategoria());
    }

    // Metodo ausiliario per creare un oggetto Prodotto da un ResultSet
    private Prodotto createProdottoFromResultSet(ResultSet rs) throws SQLException {
        return new Prodotto(
            rs.getString("codprodotto"),
            rs.getString("nome"),
            rs.getString("descrizione"),
            rs.getDouble("prezzo"),
            rs.getString("luogoprovenienza"),
            rs.getDate("dataraccolta"),
            rs.getDate("datamungitura"),
            rs.getBoolean("glutine"),
            rs.getDate("datascadenza"),
            rs.getString("categoria"),
            rs.getInt("scorta")
        );
    }
}
