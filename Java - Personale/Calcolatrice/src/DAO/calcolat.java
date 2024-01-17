package DAO;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Calc;

public interface calcolat {
	public boolean setNewCalc(Calc calcolatrice) throws SQLException;
	public ArrayList<Calc> getAllCalc(float s1, float s2, String s3, float s4) throws SQLException;
}