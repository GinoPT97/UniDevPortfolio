package Model;

import java.sql.Date;

public class Ordine {
	private String codordine;
	private Date datacquisto;
	private double prezzotot;
	private String idct;
	private String iddip;

	public Ordine(String codordine, Date dayord, double prezzotot, String idct, String iddip) {
		this.codordine = codordine;
		this.datacquisto = dayord;
		this.prezzotot = prezzotot;
		this.idct = idct;
		this.iddip = iddip;
	}

	public String getCodOrd() {
		return codordine;
	}

	public void setCodOrd(String codordine) {
		this.codordine = codordine;
	}

	public Date getAcquisto() {
		return datacquisto;
	}

	public void setAcquisto(Date datacquisto) {
		this.datacquisto = datacquisto;
	}

	public double getPrezzoT() {
		return prezzotot;
	}

	public void setPrezzoT(double prezzotot) {
		this.prezzotot = prezzotot;
	}

	public String getIdCt() {
		return idct;
	}

	public void setIdCt(String idct) {
		this.idct = idct;
	}

	public String getIdDip() {
		return iddip;
	}

	public void setIdDip(String iddip) {
		this.iddip = iddip;
	}

	@Override
	public String toString() {
		return (codordine + "" + datacquisto + "" + prezzotot + "" + idct + "" + iddip + "");
	}
}
