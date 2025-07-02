package daoimplementation;

import daointerface.ProdottoJDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Prodotto;

public class ProdottoImpl implements ProdottoJDBC {
    private final Connection connection;
    private PreparedStatement setNewProdottoStmt;
    private PreparedStatement getAllProdottiStmt;
    private PreparedStatement updateProdottoStmt;

    public ProdottoImpl(Connection connection) {
        this.connection = connection;
    }

    // Metodo per inizializzare le query preparate
    private void initStatements() throws SQLException {
        this.getAllProdottiStmt = connection.prepareStatement("SELECT codprodotto, nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta FROM prodotto ORDER BY nome DESC");
        this.setNewProdottoStmt = connection.prepareStatement(
                "INSERT INTO prodotto (nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta) VALUES (?, ?, ?, ?, ?, ?, CAST(? AS BOOLEAN), ?, ?::TIPOLOGIA, ?)"
        );
        this.updateProdottoStmt = connection.prepareStatement(
                "UPDATE prodotto SET nome=?, descrizione=?, prezzo=?, luogoprovenienza=?, dataraccolta=?, datamungitura=?, glutine=CAST(? AS BOOLEAN), datascadenza=?, categoria=?::TIPOLOGIA, scorta=? WHERE codprodotto = CAST(? AS INTEGER)"
        );
    }

    @Override
    public boolean setNewProdotto(Prodotto prodotto) throws SQLException {
        initStatements(); // Inizializza le query preparate
        setCommonProdottoFields(setNewProdottoStmt, prodotto);
        setNewProdottoStmt.setInt(10, prodotto.getScorta());

        boolean result = setNewProdottoStmt.executeUpdate() > 0;
        closeStatements();
        return result;
    }

    @Override
    public boolean updateProdotto(Prodotto prodotto) throws SQLException {
        initStatements(); // Inizializza le query preparate
        setCommonProdottoFields(updateProdottoStmt, prodotto);
        updateProdottoStmt.setInt(10, prodotto.getScorta());
        updateProdottoStmt.setString(11, prodotto.getCodProd());

        boolean result = updateProdottoStmt.executeUpdate() > 0;
        closeStatements();
        return result;
    }

    @Override
    public boolean updateScorte(int quantita, String codiceProdotto) throws SQLException {
        String query = "UPDATE prodotto SET scorta = scorta - ? WHERE codprodotto = CAST(? AS INTEGER)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantita);
            stmt.setString(2, codiceProdotto); // Ensure codiceProdotto is cast to INTEGER in the query
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
    }
    }

    @Override
    public ArrayList<Prodotto> getallprodotti() throws SQLException {
        initStatements(); // Inizializza le query preparate
        ArrayList<Prodotto> prodotti = new ArrayList<>();
        try (ResultSet rs = getAllProdottiStmt.executeQuery()) {
            while (rs.next()) {
                prodotti.add(createProdottoFromResultSet(rs));
            }
        }
        closeStatements();
        return prodotti;
    }

    @Override
    public ArrayList<Prodotto> getbycategoria(String categoria) throws SQLException {
        ArrayList<Prodotto> prodottiCategoria = new ArrayList<>();
        // Cast esplicito per confrontare ENUM con stringa
        String query = "SELECT codprodotto, nome, descrizione, prezzo, luogoprovenienza, dataraccolta, datamungitura, glutine, datascadenza, categoria, scorta FROM prodotto WHERE categoria::text = ? ORDER BY nome DESC";
        try (PreparedStatement getCategoriaStmt = connection.prepareStatement(query)) {
            getCategoriaStmt.setString(1, categoria);
            try (ResultSet rs = getCategoriaStmt.executeQuery()) {
                while (rs.next()) {
                    prodottiCategoria.add(createProdottoFromResultSet(rs));
                }
            }
        }
        return prodottiCategoria;
    }

    private void setCommonProdottoFields(PreparedStatement stmt, Prodotto prodotto) throws SQLException {
        stmt.setString(1, prodotto.getNome());
        stmt.setString(2, prodotto.getDescrizione());
        stmt.setDouble(3, prodotto.getPrezzo());
        stmt.setString(4, prodotto.getLuogoProv());

        switch (prodotto.getCategoria()) {
            case "Ortofrutticoli" -> {
                stmt.setDate(5, new java.sql.Date(prodotto.getDataraccolta().getTime()));
                stmt.setDate(6, null); // datamungitura deve essere NULL
                stmt.setNull(7, java.sql.Types.BOOLEAN); // glutine deve essere NULL
                stmt.setDate(8, null); // datascadenza deve essere NULL
            }
            case "Inscatolati" -> {
                stmt.setDate(5, null); // dataraccolta deve essere NULL
                stmt.setDate(6, null); // datamungitura deve essere NULL
                stmt.setNull(7, java.sql.Types.BOOLEAN); // glutine deve essere NULL
                stmt.setDate(8, new java.sql.Date(prodotto.getDatascadenza().getTime()));
            }
            case "Latticini" -> {
                stmt.setDate(5, null); // dataraccolta deve essere NULL
                stmt.setDate(6, new java.sql.Date(prodotto.getDatamungitura().getTime()));
                stmt.setNull(7, java.sql.Types.BOOLEAN); // glutine deve essere NULL
                stmt.setDate(8, null); // datascadenza deve essere NULL
            }
            case "Farinacei" -> {
                stmt.setDate(5, null); // dataraccolta deve essere NULL
                stmt.setDate(6, null); // datamungitura deve essere NULL
                stmt.setBoolean(7, prodotto.isGlutine()); // glutine deve essere NOT NULL
                stmt.setDate(8, null); // datascadenza deve essere NULL
            }
            default -> {
                // Categoria non riconosciuta - imposta tutti i campi opzionali a null
                stmt.setDate(5, null);
                stmt.setDate(6, null);
                stmt.setNull(7, java.sql.Types.BOOLEAN);
                stmt.setDate(8, null);
            }
        }
        stmt.setString(9, prodotto.getCategoria());
    }

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

    // Metodo per chiudere le dichiarazioni preparate
    private void closeStatements() throws SQLException {
        if (setNewProdottoStmt != null) {
            setNewProdottoStmt.close();
        }
        if (getAllProdottiStmt != null) {
            getAllProdottiStmt.close();
        }
        if (updateProdottoStmt != null) {
            updateProdottoStmt.close();
        }
    }
}

