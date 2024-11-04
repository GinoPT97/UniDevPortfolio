import java.io.InputStream;
import java.util.Properties;

public class DAOFactory {
	
	String db;
	
	private static DAOFactory theDAO;
	
	
	public static synchronized DAOFactory getDAOInstance() {
		if (theDAO==null)
			theDAO= new DAOFactory();
		
		return theDAO;
		
	}
	
	
	
	private DAOFactory() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

		    input = getClass().getClassLoader().getResourceAsStream("config.properties");
		    prop.load(input);
		    db=prop.getProperty("database");
		    System.out.println(db);
		    
		} catch (Exception ex) {
		    ex.printStackTrace();
		} 
		
	}
	
	
	public StudenteDAO getStudenteDAO() {
		if (db.equals("postgres"))
			return new StudentePostgresDAO();
	
		
		if (db.equals("AWS"))
			return new StudenteAWSDAO();
		
		
		return null;
	}

}
