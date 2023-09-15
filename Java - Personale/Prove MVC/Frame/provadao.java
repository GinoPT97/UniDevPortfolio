package provadao;

import java.util.*;

import javax.swing.table.DefaultTableModel;

import entita.provaentita;

import java.sql.*;

public interface provadao {
	public ArrayList<provaentita> getAllprova() throws SQLException;
	public boolean updateprova(String id, String nome, String contatto, String corso) throws SQLException;
    public boolean inserisciprove(provaentita pe) throws SQLException;
    public boolean cancellaprove(String id) throws SQLException;
}
