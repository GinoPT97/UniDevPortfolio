package DAOImplementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DAOInterface.DipendenteJDBC;
import Model.Dipendente;

public class DipendenteImpl implements DipendenteJDBC {
    private PreparedStatement setNewDip, updateDip;
    private Statement getAllDip, getDipVendite, getDipIntroiti, verifyId, getDip;
    private List<String> ordven = new ArrayList<>();
    private List<String> ordint = new ArrayList<>();

    // Costruttore
    public DipendenteImpl(Connection connection) throws SQLException {
        getAllDip = connection.createStatement();
        setNewDip = connection.prepareStatement(
                "INSERT INTO dipendente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES (?, ?, ?, ?, ?, ?)");
        updateDip = connection.prepareStatement(
                "UPDATE dipendente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE coddipendente = ?");
        getDip = connection.createStatement();
        verifyId = connection.createStatement();
        getDipVendite = connection.createStatement();
        getDipIntroiti = connection.createStatement();
    }

    @Override
    public boolean setNewDip(Dipendente dipendente) throws SQLException {
        setPreparedStatement(setNewDip, dipendente);
        return setNewDip.executeUpdate() > 0;
    }

    @Override
    public boolean verifyID(String id) throws SQLException {
        String query = "SELECT coddipendente FROM dipendente WHERE coddipendente = ?";
        try (PreparedStatement ps = verifyId.getConnection().prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public ArrayList<Dipendente> getAllDip() throws SQLException {
        ArrayList<Dipendente> dipendenti = new ArrayList<>();
        try (ResultSet rs = getAllDip.executeQuery("SELECT * FROM dipendente ORDER BY cognome DESC")) {
            while (rs.next()) {
                dipendenti.add(new Dipendente(
                        rs.getString("coddipendente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono")));
            }
        }
        return dipendenti;
    }

    @Override
    public List<String> getDipVendite(Date di, Date df) throws SQLException {
        ordven.clear();
        String query = "SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini "
                + "FROM dipendente AS D, ordine AS O "
                + "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome "
                + "ORDER BY Tordini DESC "
                + "LIMIT 1";
        try (PreparedStatement ps = getDipVendite.getConnection().prepareStatement(query)) {
            ps.setDate(1, di);
            ps.setDate(2, df);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ordven.add(rs.getString("nome"));
                    ordven.add(rs.getString("cognome"));
                    ordven.add(rs.getString("Tordini"));
                }
            }
        }
        return ordven;
    }

    @Override
    public List<String> getDipIntroiti(Date di, Date df) throws SQLException {
        ordint.clear();
        String query = "SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine "
                + "FROM dipendente AS D, ordine AS O "
                + "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome "
                + "ORDER BY Sordine DESC "
                + "LIMIT 1";
        try (PreparedStatement ps = getDipIntroiti.getConnection().prepareStatement(query)) {
            ps.setDate(1, di);
            ps.setDate(2, df);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ordint.add(rs.getString("nome"));
                    ordint.add(rs.getString("cognome"));
                    ordint.add(rs.getString("Sordine"));
                }
            }
        }
        return ordint;
    }

    @Override
    public boolean updatedipendente(Dipendente dipendente) throws SQLException {
        setPreparedStatement(updateDip, dipendente);
        updateDip.setInt(7, Integer.parseInt(dipendente.getCodDIP()));
        return updateDip.executeUpdate() > 0;
    }

    @Override
    public Dipendente getOneDip(String id) throws SQLException {
        Dipendente dipendente = null;
        String query = "SELECT * FROM dipendente WHERE coddipendente = ?";
        try (PreparedStatement ps = getDip.getConnection().prepareStatement(query)) {
            // Verifica se 'id' è un intero e fai il cast
            int idInt = Integer.parseInt(id);  // Assicurati che 'id' sia numerico
            ps.setInt(1, idInt);  // Imposta l'ID come intero
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dipendente = new Dipendente(
                            id,  // Restituisci l'id come String
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("codicefiscale"),
                            rs.getString("email"),
                            rs.getString("indirizzo"),
                            rs.getString("telefono"));
                }
            }
        }
        return dipendente;
    }

    // Metodo di supporto per evitare codice ripetitivo nella preparazione delle query
    private void setPreparedStatement(PreparedStatement ps, Dipendente dipendente) throws SQLException {
        ps.setString(1, dipendente.getNome());
        ps.setString(2, dipendente.getCognome());
        ps.setString(3, dipendente.getCodFis());
        ps.setString(4, dipendente.getInd());
        ps.setString(5, dipendente.getTel());
        ps.setString(6, dipendente.getEmail());
    }
}

