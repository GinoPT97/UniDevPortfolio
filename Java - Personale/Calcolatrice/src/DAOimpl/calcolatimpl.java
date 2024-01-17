package DAOimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.calcolat;
import Model.Calc;

public class calcolatimpl implements calcolat {
	private Connection connection;
	private PreparedStatement setNewCalc,getAllCalc;
	private ArrayList<Calc> calcal = new ArrayList<Calc>();
	
	public calcolatimpl(Connection connection) throws SQLException{
		this.connection = connection;
		setNewCalc = connection.prepareStatement("INSERT INTO calcolatrice VALUES (?, ?, ?, ?)");
		getAllCalc = connection.prepareStatement("SELECT * FROM calcolatrice");
	}

	@Override
	public boolean setNewCalc(Calc calcolatrice) throws SQLException {
		setNewCalc.setFloat(1, calcolatrice.getOp1());
		setNewCalc.setFloat(2, calcolatrice.getOp2());
		setNewCalc.setString(3, calcolatrice.getOperator());
		setNewCalc.setFloat(4, calcolatrice.getRis());
        int row = setNewCalc.executeUpdate();
        if(row<1) return true;
        else return false;
	}

	@Override
	public ArrayList<Calc> getAllCalc(float s1, float s2, String s3, float s4) throws SQLException {
		ResultSet rs = getAllCalc.executeQuery();
        while(rs.next())
        {
        	calcal.add(new Calc(rs.getFloat("operando1"), rs.getFloat("operando"),rs.getString("operatore"), rs.getFloat("risultato")));
        }
        rs.close();
        return calcal;
	}
}
