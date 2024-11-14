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
    private PreparedStatement setNewDip, updateDip, verifyIdPS, getDipVenditePS, getDipIntroitiPS, getOneDipPS;
    private Statement getAllDip;
    private List<String> ordven = new ArrayList<>();
    private List<String> ordint = new ArrayList<>();

    // Costruttore
    public DipendenteImpl(Connection connection) throws SQLException {
        getAllDip = connection.createStatement();
        setNewDip = connection.prepareStatement(
                "INSERT INTO dipendente VALUES (NEXTVAL('SCodDipendente'), ?, ?, ?, ?, ?, ?)");
        updateDip = connection.prepareStatement(
                "UPDATE dipendente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE coddipendente = ?");
        verifyIdPS = connection.prepareStatement("SELECT coddipendente FROM dipendente WHERE coddipendente = ?");
        getDipVenditePS = connection.prepareStatement(
                "SELECT DISTINCT D.nome, D.cognome, COUNT(O) AS Tordini "
                + "FROM dipendente AS D, ordine AS O "
                + "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome "
                + "ORDER BY Tordini DESC "
                + "LIMIT 1");
        getDipIntroitiPS = connection.prepareStatement(
                "SELECT DISTINCT D.nome, D.cognome, SUM(O.prezzototale) AS Sordine "
                + "FROM dipendente AS D, ordine AS O "
                + "WHERE O.coddipendente = D.coddipendente AND O.dataacquisto BETWEEN ? AND ? "
                + "GROUP BY D.nome, D.cognome "
                + "ORDER BY Sordine DESC "
                + "LIMIT 1");
        getOneDipPS = connection.prepareStatement("SELECT * FROM dipendente WHERE coddipendente = ?");
    }

    @Override
    public boolean setNewDip(Dipendente dipendente) throws SQLException {
        setPreparedStatement(setNewDip, dipendente);
        return setNewDip.executeUpdate() > 0;
    }

    @Override
    public boolean verifyID(String id) throws SQLException {
        verifyIdPS.setString(1, id);
        try (ResultSet rs = verifyIdPS.executeQuery()) {
            return rs.next();
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
        getDipVenditePS.setDate(1, di);
        getDipVenditePS.setDate(2, df);
        try (ResultSet rs = getDipVenditePS.executeQuery()) {
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
        ordint.clear();
        getDipIntroitiPS.setDate(1, di);
        getDipIntroitiPS.setDate(2, df);
        try (ResultSet rs = getDipIntroitiPS.executeQuery()) {
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

    public Dipendente getOneDip(String codDip) throws SQLException {
        getOneDipPS.setInt(1, Integer.parseInt(codDip));
        try (ResultSet rs = getOneDipPS.executeQuery()) {
            if (rs.next()) {
                return new Dipendente(
                    rs.getString("coddipendente"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("indirizzo"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("ruolo")
                );
            }
        }
        return null;
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

