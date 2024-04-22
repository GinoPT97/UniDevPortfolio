package it.polimi.awt.jpa;

import java.util.*;
import javax.persistence.*;

public class ListProjects {

	public static void main(String[] args) {
		
		// Create a new persistence context
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeUnit");
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Project> q = em.createQuery("SELECT p FROM Project p", Project.class);
		List<Project> projects = q.getResultList();
		
		for (Project p: projects) {
			System.out.println(p.getCode() + " " + p.getName());
			
			for (Employee e: p.getEmployees()) 
				System.out.println("\t" + e.getId() + " " + e.getSurname());			
		
			System.out.println();
		}
		
		em.close();
		emf.close();
		
	}

}
