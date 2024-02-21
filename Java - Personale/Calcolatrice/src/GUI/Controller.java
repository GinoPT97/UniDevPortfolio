package GUI;

import java.sql.Connection;
import java.sql.SQLException;

import DAO.calcolat;
import DAOimpl.calcolatimpl;
import DBConfig.ConnectionException;
import DBConfig.DBConfiguration;
import DBConfig.DBConnect;
import Model.Calc;
import Model.Model;

public class Controller {
    private Model m;
    private Calcolatrice calcf;
    private PrincFrame princf;
    private StoricoFrame storicof;
    private String ris;
    private calcolat calc = null;
    private DBConnect dbconn = null;
    private Connection connection = null;
    private DBConfiguration config = null;

	public Controller() {
		m = new Model();
		calcf = new Calcolatrice("Calcolatrice", this);
		princf = new PrincFrame("Main Menu", this);
		storicof = new StoricoFrame("Storico", this);
	}

	public String varcast(float x) {
		String y = String.valueOf(x);
		return y;
	}

	public String somma() {
		float c = m.somma(calcf.first(), calcf.second());
		return ris = varcast(c);
	}

	public String sottrazione() {
		float c = m.sottrazione(calcf.first(), calcf.second());
		return ris = varcast(c);
	}

	public String moltiplicazione() {
		float c = m.moltiplicazione(calcf.first(), calcf.second());
		return ris = varcast(c);
	}

	public String divisione() {
		float c = m.divisione(calcf.first(), calcf.second());
		return ris = varcast(c);
	}

	public void calc() {
		princf.setVisible(false);
		calcf.setVisible(true);
	}

	public void storico() {
		princf.setVisible(false);
		storicof.setVisible(true);
	}

	public void back1() {
		princf.setVisible(true);
		calcf.setVisible(false);
	}

	public void back2() {
		princf.setVisible(true);
		storicof.setVisible(false);
	}

	public static void main(String[] args) throws SQLException {
		Controller c = new Controller();
		c.DB();
	}

	private void DB () throws SQLException{
	try {
		dbconn = DBConnect.getInstance("postgres");
        connection = dbconn.getConnection();
        config = new DBConfiguration(connection);
        config.createTableCalcolatrice();
        calc = new calcolatimpl(connection);
	}
    catch (SQLException exception)
    {
        System.out.println("SQLException: "+ exception.getMessage());
    }
    catch (ConnectionException ex)
    {
        System.out.println("CE: "+ex);
    }
	}

	public Calc setop(Calc cal) throws SQLException {
		calc.setNewCalc(cal);
		return cal;
	}
}