package DAOImplementations;

import DAO.DipendenteDAO;
import DBEntities.Dipendente;
import java.sql.*;
import java.util.ArrayList;

public class DipendenteDAOPostgreImpl implements DipendenteDAO {

    private ArrayList<Dipendente> dipendenti = new ArrayList<Dipendente>();
    private Connection connection;
    private Statement checkDipendenteID;
    private Statement getDipendenti;
    private Statement filterDipendenti;
    private PreparedStatement insertDipendente;
    private PreparedStatement updateDipendente;


    public DipendenteDAOPostgreImpl(Connection connection) throws SQLException {
        this.connection = connection;
        checkDipendenteID = connection.createStatement();
        getDipendenti = connection.createStatement();
        filterDipendenti = connection.createStatement();
        insertDipendente = connection.prepareStatement("INSERT INTO DIPENDENTE VALUES (NEXTVAL('SqCodDipendente'), ?, ?, ?, ?, ?, null, ?);");
        updateDipendente = connection.prepareStatement("UPDATE DIPENDENTE SET Nome = ?,\n" +
                "Cognome = ?,\n" +
                "CodiceFiscale = ?,\n" +
                "Indirizzo = ?,\n" +
                "Telefono1 = ?,\n" +
                "Email = ?\n" +
                "WHERE CodDipendente = ?;");
    }

    //Controlla l'ID di un dipendente quando si effettua l'accesso
    public boolean checkDipendenteID(String ID) throws SQLException {
        ResultSet rs = checkDipendenteID.executeQuery("SELECT CodDipendente FROM DIPENDENTE WHERE CodDipendente = '" + ID + "';");
        if (rs.next())
            if (rs.getString("CodDipendente").equals(ID))
                return true;
        return false;
    }

    @Override
    public boolean insertDipendente(Dipendente dipendente) throws SQLException {
        insertDipendente.setString(1, dipendente.getNome());
        insertDipendente.setString(2, dipendente.getCognome());
        insertDipendente.setString(3, dipendente.getCodiceFiscale());
        insertDipendente.setString(4, dipendente.getIndirizzo());
        if (dipendente.getTelefono().equals(""))
            insertDipendente.setString(5, null);
        else
            insertDipendente.setString(5, dipendente.getTelefono());
        insertDipendente.setString(6, dipendente.getEmail());
        int rows = insertDipendente.executeUpdate();
        if (rows < 1)
            return false;
        else
            return true;
    }

    @Override
    public ArrayList<Dipendente> getAllDipendenti() {
        dipendenti.clear();
        try {
            ResultSet rs = getDipendenti.executeQuery("SELECT * FROM DIPENDENTE");
            while (rs.next()) {
                dipendenti.add(new Dipendente(rs.getString("CodDipendente"), rs.getString("Nome"),
                        rs.getString("Cognome"), rs.getString("CodiceFiscale"), rs.getString("Indirizzo"),
                        rs.getString("Telefono1"), rs.getString("Email")));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
        }
        return dipendenti;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListByComune(String comune) {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListByAnnoNascita(String anno) {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListByCognome(String cognome) {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListByNome(String nome) {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListByNomeCognome(String nome, String cognome) {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListPerVendite() {
        return null;
    }

    @Override
    public ArrayList<Dipendente> getDipendentiListPerRicavi() {
        return null;
    }

    @Override
    public boolean updateDipendente(String id, String n, String ln, String cf, String a, String p, String e) throws SQLException {
        updateDipendente.setString(1, n);
        updateDipendente.setString(2, ln);
        updateDipendente.setString(3, cf);
        updateDipendente.setString(4, a);
        updateDipendente.setString(5, p);
        updateDipendente.setString(6, e);
        updateDipendente.setString(7, id);
        int rows = updateDipendente.executeUpdate();
        if (rows < 1)
            return false;
        else
            return true;
    }

    public ArrayList<Dipendente> filterDipendentiListBy(String filter) {
        dipendenti.clear();
        if (filter.equals("Codice Dipendente"))
            filter = "CodDipendente";
        try {
            ResultSet rs = filterDipendenti.executeQuery("SELECT * FROM DIPENDENTE ORDER BY " + filter + " ASC");
            while (rs.next()) {
                dipendenti.add(new Dipendente(rs.getString("CodDipendente"), rs.getString("Nome"),
                        rs.getString("Cognome"), rs.getString("CodiceFiscale"), rs.getString("Indirizzo"),
                        rs.getString("Telefono1"), rs.getString("Email")));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
        }
        return dipendenti;
    }
}
