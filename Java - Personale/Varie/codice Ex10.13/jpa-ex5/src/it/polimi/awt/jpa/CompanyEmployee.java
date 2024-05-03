package it.polimi.awt.jpa;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CompanyEmployee")
public class CompanyEmployee extends Employee {

	private Integer companyId;
	private Double salary;
	
	public Integer getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Double getSalary() {
		return salary;
	}
	
	public void setSalary(Double salary) {
		this.salary = salary;
	}

}
