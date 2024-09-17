package DAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.provadao;
import Entita.provaentita;

public class provadaoimpl implements provadao{

	private Connection connection;
    private PreparedStatement getprove, inserisciprove, updateprove, cancellaprove;
    private ArrayList<provaentita> pe = new ArrayList<>();

    public provadaoimpl(Connection connection) throws SQLException {
        this.connection = connection;
        cancellaprove = connection.prepareStatement("DELETE FROM prove WHERE id = ?");
        getprove = connection.prepareStatement("SELECT * FROM prove ORDER BY corso DESC");
        inserisciprove = connection.prepareStatement("INSERT INTO prove VALUES (NEXTVAL('seqid'), ?, ?, ?)");
        updateprove = connection.prepareStatement("UPDATE prove SET nome = ?, contatto = ?, corso = ? WHERE id = ?");
    }

	@Override
	public boolean cancellaprove(String id) throws SQLException {
		cancellaprove.setString(1, id);
		int row = cancellaprove.executeUpdate();
        if(row>0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<provaentita> getAllprova() throws SQLException {
        ResultSet rs = getprove.executeQuery();
        while(rs.next()) {
			pe.add(new provaentita(rs.getString("id"),rs.getString("nome"), rs.getString("contatto"),rs.getString("corso")));
		}
        rs.close();
        return pe;
	}

	@Override
	public boolean inserisciprove(provaentita pe) throws SQLException {
		//inserisciprove.setString(1, pe.getid());
		inserisciprove.setString(1, pe.getnome());
        inserisciprove.setString(2, pe.getcontatto());
        inserisciprove.setString(3, pe.getcorso());
        int row = inserisciprove.executeUpdate();
        if(row<1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateprova(String id, String nome, String contatto, String corso) throws SQLException {
		updateprove.setString(1, nome);
		updateprove.setString(2, contatto);
		updateprove.setString(3, corso);
		updateprove.setString(4, id);
        int row = updateprove.executeUpdate();
        if(row>0) {
			return true;
		} else {
			return false;
		}
	}

}
