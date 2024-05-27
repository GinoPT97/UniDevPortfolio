import java.util.List;

public interface StudenteDAO {
	
	public boolean SalvaStudente(Studente s);
	
	public boolean RimuoviStudente(Studente s);
	
	public boolean AggiornaStudente(Studente s);
	
	public Studente getStudenteByMatricola(String matricola);
	
	public List<Studente> getStudenteByCorso(String corso);
	
	public List<Studente> getStudenteByCognome(String cognome);
	
}
