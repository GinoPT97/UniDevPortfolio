package it.polimi.awt.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JavaClient {
	
	public static void main(String[] args) {
		
		// Create a new persistence context
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeUnit");
		EntityManager em = emf.createEntityManager();
		
		// Projects
		Project p1 = new Project();
		p1.setCode(1);
		p1.setName("CUbRIK");
		
		Project p2 = new Project();
		p2.setCode(2);
		p2.setName("REWIND");
		
		// Employees
		Employee e1 = new Employee();
		e1.setId(1);
		e1.setName("John");
		e1.setSurname("Doe");
		
		Employee e2 = new Employee();
		e2.setId(2);
		e2.setName("John");
		e2.setSurname("Smith");
		
		Employee e3 = new Employee();
		e3.setId(3);
		e3.setName("Jane");
		e3.setSurname("Doe");
		
		Employee e4 = new Employee();
		e4.setId(4);
		e4.setName("Sammy");
		e4.setSurname("Soe");
		
		// Branches
		Branch b1 = new Branch();
		b1.setCity("Milano");
		b1.setName("Politecnico di Milano");
		b1.setAddress("Piazzale Leonardo Da Vinci 32");
		
		Branch b2 = new Branch();
		b2.setCity("Como");
		b2.setName("Politecnico di Milano, sede di Como");
		b2.setAddress("Via Valleggo 11");

		
		// Domain model graph 
		p1.addEmployee(e1);
		p1.addEmployee(e2);
		p1.addBranch(b1);
		p1.addBranch(b2);
		
		e1.setProject(p1);
		e2.setProject(p1);
		b1.addProject(p1);
		b2.addProject(p1);
		
		p2.addEmployee(e3);
		p2.addEmployee(e4);
		p2.addBranch(b2);
		
		e3.setProject(p2);
		e4.setProject(p2);
		b2.addProject(p2);
		
		// Persist the projects
		em.getTransaction().begin();
		em.persist(p1);
		em.persist(p2);
		em.getTransaction().commit();
		
		// Close resources
		em.close();
		emf.close();
	}

}
