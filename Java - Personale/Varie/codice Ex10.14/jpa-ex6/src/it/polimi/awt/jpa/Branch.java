package it.polimi.awt.jpa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Branch {
	
	@Id
	private String name;
	private String city;
	private String address;
	
	@ManyToMany
	private List<Project> projects;
	
	public Branch() {
		this.projects = new ArrayList<Project>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
	}
}
