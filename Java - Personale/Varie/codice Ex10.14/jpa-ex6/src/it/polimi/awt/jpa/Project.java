package it.polimi.awt.jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project implements Serializable {

	private static final long serialVersionUID = 2355730818150131253L;

	@Id
	private Integer code;
	private String name;
	
	@OneToMany(mappedBy="project", cascade=CascadeType.PERSIST)
	private List<Employee> employees;
	
	@ManyToMany(mappedBy="projects", cascade=CascadeType.PERSIST)
	private List<Branch> branches;
	
	public Project() {
		this.employees = new ArrayList<Employee>();
		this.branches = new ArrayList<Branch>();
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Employee> getEmployees() {
		return employees;
	}

	public void addEmployee(Employee employee) {
		this.employees.add(employee);
	}
		
	public void addBranch(Branch branch) {
		this.branches.add(branch);
	}
}
