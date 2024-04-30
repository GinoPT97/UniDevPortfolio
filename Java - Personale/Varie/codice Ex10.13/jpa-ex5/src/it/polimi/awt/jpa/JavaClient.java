package it.polimi.awt.jpa;

import javax.persistence.*;

public class JavaClient {

	public static void main(String[] args) {
		
		// Create a new persistence context
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeUnit");
		EntityManager em = emf.createEntityManager();
	
		// Create Company Employees
		CompanyEmployee ce1 = new CompanyEmployee();
		CompanyEmployee ce2 = new CompanyEmployee();
		
		// Create Contract Employees
		ContractEmployee ce3 = new ContractEmployee();
		ContractEmployee ce4 = new ContractEmployee();
		
		// Domain model
		ce1.setName("John");
		ce1.setSurname("Doe");
		ce1.setCompanyId(1);
		ce1.setDateOfBirth(DateUtil.parseDate("07/12/1973"));
		ce1.setSalary(1543.27);
		
		ce2.setName("Adrian");
		ce2.setSurname("Smith");
		ce2.setCompanyId(2);
		ce2.setDateOfBirth(DateUtil.parseDate("04/18/1987"));
		ce2.setSalary(1348.12);
		
		ce3.setName("Sammy");
		ce3.setSurname("Soe");
		ce3.setDateOfBirth(DateUtil.parseDate("10/03/1981"));
		ce3.setHourlyRate(20.00);
		ce3.setVATNumber("122-123-34-344");
		
		ce4.setName("Steve");
		ce4.setSurname("McBrain");
		ce4.setDateOfBirth(DateUtil.parseDate("12/12/1961"));
		ce4.setHourlyRate(25.5);
		ce4.setVATNumber("934-491-85-432");
		
		// Persist
		em.getTransaction().begin();
		em.persist(ce1);
		em.persist(ce2);
		em.persist(ce3);
		em.persist(ce4);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
	}

}
