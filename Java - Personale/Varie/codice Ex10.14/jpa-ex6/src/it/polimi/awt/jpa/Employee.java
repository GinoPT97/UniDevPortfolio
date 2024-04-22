package it.polimi.awt.jpa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Employee implements Serializable {

	private static final long serialVersionUID = 6911901754883254958L;
	
	@Id
	private Integer id;
	private String name;
	private String surname;
	
	@ManyToOne
	private Project project;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
