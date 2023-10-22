package DAO;

import DBEntities.Dipendente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DipendenteDAO {

    //Input:
    public boolean insertDipendente(Dipendente dipendente) throws SQLException;
    public boolean checkDipendenteID(String id) throws SQLException;

    //Output:
    public ArrayList<Dipendente> getAllDipendenti();
    public ArrayList<Dipendente> getDipendentiListByComune(String comune);
    public ArrayList<Dipendente> getDipendentiListByAnnoNascita(String anno);
    public ArrayList<Dipendente> getDipendentiListByCognome(String cognome);
    public ArrayList<Dipendente> getDipendentiListByNome(String nome);
    public ArrayList<Dipendente> getDipendentiListByNomeCognome(String nome, String cognome);
    public ArrayList<Dipendente> getDipendentiListPerVendite();
    public ArrayList<Dipendente> getDipendentiListPerRicavi();
    public boolean updateDipendente(String id, String n, String ln, String cf, String a, String p, String e) throws SQLException;
}
