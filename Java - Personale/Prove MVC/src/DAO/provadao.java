package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

import Entita.provaentita;

public interface provadao {
	public boolean cancellaprove(String id) throws SQLException;
	public ArrayList<provaentita> getAllprova() throws SQLException;
    public boolean inserisciprove(provaentita pe) throws SQLException;
    public boolean updateprova(String id, String nome, String contatto, String corso) throws SQLException;
}
