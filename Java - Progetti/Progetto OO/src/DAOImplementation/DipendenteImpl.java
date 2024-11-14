package DAOImplementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAOInterface.DipendenteJDBC;
import Model.Dipendente;

public class DipendenteImpl implements DipendenteJDBC {
    private final PreparedStatement setNewDip;
    private final PreparedStatement updateDip;
    private final PreparedStatement getAllDip;
    private final PreparedStatement getDipVendite;
    private final PreparedStatement getDipIntroiti;
    private final PreparedStatement verifyId;
    private final PreparedStatement getDip;

    // Costruttore
    public DipendenteImpl(Connection connection) throws SQLException {

        // Prepara tutte le query SQL
        setNewDip = connection.prepareStatement("INSERT INTO dipendente (coddipendente, nome, cognome, codicefiscale, indirizzo, telefono, email) "
                + "VALUES (nextval('SCodDipendente'), ?, ?, ?, ?, ?, ?)");

        updateDip = connection.prepareStatement("UPDATE dipendente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? "
                + "WHERE coddipendente = ?");

        getAllDip = connection.prepareStatement("SELECT * FROM dipendente ORDER BY cognome DESC");

        verifyId = connection.prepareStatement("SELECT coddipendente FROM dipendente WHERE coddipendente = ?");

        getDip = connection.prepareStatement("SELECT * FROM dipendente WHERE coddipendente = ?");

        getDipVendite = connection.prepareStatement("SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini "
                + "FROM dipendente AS D JOIN ordine AS O ON O.coddipendente = D.coddipendente "
                + "WHERE O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome ORDER BY Tordini DESC LIMIT 1");

        getDipIntroiti = connection.prepareStatement("SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine "
                + "FROM dipendente AS D JOIN ordine AS O ON O.coddipendente = D.coddipendente "
                + "WHERE O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome ORDER BY Sordine DESC LIMIT 1");
    }

    @Override
    public boolean setNewDip(Dipendente dipendente) throws SQLException {
        setPreparedStatement(setNewDip, dipendente);
        return setNewDip.executeUpdate() > 0;
    }

    @Override
    public boolean verifyID(String id) throws SQLException {
        verifyId.setString(1, id);
        try (ResultSet rs = verifyId.executeQuery()) {
            return rs.next();
        }
    }

    @Override
    public ArrayList<Dipendente> getAllDip() throws SQLException {
        ArrayList<Dipendente> dipendenti = new ArrayList<>();
        try (ResultSet rs = getAllDip.executeQuery()) {
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
        List<String> ordven = new ArrayList<>();
        getDipVendite.setDate(1, di);
        getDipVendite.setDate(2, df);
        try (ResultSet rs = getDipVendite.executeQuery()) {
            if (rs.next()) {
                ordven.add(rs.getString("nome"));
                ordven.add(rs.getString("cognome"));
                ordven.add(rs.getString("Tordini"));
            }
        }
        return ordven;
    }

    @Override
    public List<String> getDipIntroiti(Date di, Date df) throws SQLException {
        List<String> ordint = new ArrayList<>();
        getDipIntroiti.setDate(1, di);
        getDipIntroiti.setDate(2, df);
        try (ResultSet rs = getDipIntroiti.executeQuery()) {
            if (rs.next()) {
                ordint.add(rs.getString("nome"));
                ordint.add(rs.getString("cognome"));
                ordint.add(rs.getString("Sordine"));
            }
        }
        return ordint;
    }

    @Override
    public boolean updatedipendente(Dipendente dipendente) throws SQLException {
        setPreparedStatement(updateDip, dipendente);
        updateDip.setString(7, dipendente.getCodDIP());
        return updateDip.executeUpdate() > 0;
    }

    @Override
    public Dipendente getOneDip(String id) throws SQLException {
        Dipendente dipendente = null;
        getDip.setString(1, id);
        try (ResultSet rs = getDip.executeQuery()) {
            if (rs.next()) {
                dipendente = new Dipendente(
                        id,
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"));
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

