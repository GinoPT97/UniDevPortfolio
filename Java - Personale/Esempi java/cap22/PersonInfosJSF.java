package com.pellegrinoprincipe;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="pinfo")
@SessionScoped
public class PersonInfosJSF
{
    // proprietà
    private String first_name;
    private String last_name;
    private int age;
    private String address;
    private String job;

    public PersonInfosJSF() {}    
    
    // metodi getter/setter
    public String getFirst_name() { return first_name;}
    public void setFirst_name(String first_name) { this.first_name = first_name;}
    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address){ this.address = address; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
}
