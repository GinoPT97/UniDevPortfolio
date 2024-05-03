package it.polimi.awt.jpa;

import javax.persistence.*;

@Entity
@DiscriminatorValue("ContractEmployee")
public class ContractEmployee extends Employee {

	private Double hourlyRate;
	private String VATNumber;
	
	public Double getHourlyRate() {
		return hourlyRate;
	}
	public void setHourlyRate(Double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	public String getVATNumber() {
		return VATNumber;
	}
	public void setVATNumber(String vATNumber) {
		VATNumber = vATNumber;
	}
	
}
