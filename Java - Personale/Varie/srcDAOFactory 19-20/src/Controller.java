
public class Controller {

	public StudenteDAO StudDAO;
	
	public static void main(String[] args) {
		Controller c = new Controller();
	}
	
	
	public Controller() {
		DAOFactory DF = DAOFactory.getDAOInstance();
		
		StudDAO = DF.getStudenteDAO();
		
		StudDAO.getStudenteByCognome("Esposito");
		
	}

}
