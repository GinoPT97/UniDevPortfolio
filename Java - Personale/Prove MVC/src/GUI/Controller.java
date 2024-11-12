package GUI;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import DAOImpl.provadaoimpl;
import DBconfig.ConnectionException;
import DBconfig.DBConfig;
import DBconfig.DBConnection;
import Entita.provaentita;

public class Controller {
	public static void main(String[] args) throws SQLException, ConnectionException {
		 new Controller();
	}
	private Prova pf;
	private Connection connection = null;
	private DBConnection dbconn = null;
	private provadaoimpl pdao = null;

	private DBConfig builder = null;

	public Controller() throws SQLException, ConnectionException  {
		pf = new Prova("Prova",this);
		pf.setVisible(true);
	}

	public void connect() throws SQLException {
	     try{
	            dbconn = DBConnection.getInstance("postgres");
	            connection = dbconn.getConnection();
	            builder = new DBConfig(connection);
	            builder.createTableprova();
	            builder.createSequence();
				pdao = new provadaoimpl(connection);
	        }
	        catch (SQLException | ConnectionException e){
	            System.out.println("SQLException: "+ e.getMessage());
	            e.printStackTrace();
	        }
	}

	public boolean elimina(String id) throws SQLException {
		return pdao.cancellaprove(id);
	}

	public void getAllProve(DefaultTableModel model) throws SQLException {
			for(provaentita p : pdao.getAllprova()) {
				Object[] pr = {p.getid(),p.getnome(),p.getcontatto(),p.getcorso()};
				model.insertRow(0, pr);
			}
    }

	public boolean getprove(provaentita pe) throws SQLException {
		return pdao.inserisciprove(pe);
	}

    public boolean upprove(provaentita pe) throws SQLException {
		return pdao.updateprova(pe.getid(),pe.getnome(),pe.getcontatto(),pe.getcorso());
	}
}
