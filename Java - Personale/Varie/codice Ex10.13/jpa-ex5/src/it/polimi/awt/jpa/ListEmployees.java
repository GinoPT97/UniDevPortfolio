package it.polimi.awt.jpa;

import java.util.*;
import javax.persistence.*;

public class ListEmployees {

	public static void main(String[] args) {
	
		// Create a new persistence context
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeUnit");
		EntityManager em = emf.createEntityManager();
		
		// Query
		TypedQuery<Employee> q = em.createQuery("SELECT e FROM Employee e ORDER BY e.surname ASC", Employee.class);
		List<Employee> employees = q.getResultList();
		
		for (Employee e: employees) {
			
			System.out.println("Name: " + e.getName());
			System.out.println("Surname: " + e.getSurname());
			System.out.println("Date of birth: " + DateUtil.formatDate(e.getDateOfBirth()));
			
			if (e instanceof CompanyEmployee) {
				System.out.println("Employee Type: Company");
				System.out.println("Company Id: " + ((CompanyEmployee) e).getCompanyId());
				System.out.println("Salary " + ((CompanyEmployee) e).getSalary());
			} else {
				System.out.println("Employee Type: Contract");
				System.out.println("Hourly Rate: " + ((ContractEmployee) e).getHourlyRate());
				System.out.println("VAT Number: " + ((ContractEmployee) e).getVATNumber());
			}
			
			System.out.println();
		}
		
		em.close();
		emf.close();
	}

}
