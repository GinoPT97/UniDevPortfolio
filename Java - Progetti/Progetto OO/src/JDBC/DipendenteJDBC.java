package JDBC;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Dipendente;

public interface DipendenteJDBC {
	public ArrayList<Dipendente> getAllDip() throws SQLException;

	public List<String> getDipIntroiti(Date di, Date df) throws SQLException;

	public List<String> getDipVendite(Date di, Date df) throws SQLException;

	public Dipendente getOneDip(String id) throws SQLException;

	public boolean setNewDip(Dipendente dipendente) throws SQLException;

	public boolean updatedipendente(Dipendente dipendente) throws SQLException;

	public boolean verifyID(String ID) throws SQLException;
}
