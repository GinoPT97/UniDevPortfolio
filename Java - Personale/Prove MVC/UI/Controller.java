package main;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import DBconfig.*;
import daoimpl.provadaoimpl;
import entita.provaentita;

public class Controller {
	private Prova pf;
	private Connection connection = null;
	private DBConnection dbconn = null;
	private provadaoimpl pdao = null;
	private DBConfig builder = null;
	
	public Controller() throws SQLException, ConnectionException  {
		pf = new Prova("Prova",this);
		pf.setVisible(true);
	}

	public static void main(String[] args) throws SQLException, ConnectionException {
	     Controller c = new Controller();
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
    
	public boolean getprove(provaentita pe) throws SQLException {
		return pdao.inserisciprove(pe);
	}
	
	public boolean upprove(provaentita pe) throws SQLException {
		return pdao.updateprova(pe.getid(),pe.getnome(),pe.getcontatto(),pe.getcorso());
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
}
