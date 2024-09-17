package Model;

import java.sql.Date;

public class Ordine {
	private String codordine;
	private Date datacquisto;
	private double prezzotot;
	private String idct;
	private String iddip;

	public Ordine(String codordine, Date datacquisto, double prezzotot, String idct, String iddip) {
		this.codordine = codordine;
		this.datacquisto = datacquisto;
		this.prezzotot = prezzotot;
		this.idct = idct;
		this.iddip = iddip;
	}

	public String getCodOrd() {
		return codordine;
	}

	public Date getDataAcquisto() {
		return datacquisto;
	}

	public String getIdCliente() {
		return idct;
	}

	public String getIdDipendente() {
		return iddip;
	}

	public double getPrezzoTotale() {
		return prezzotot;
	}

	public void setCodOrd(String codordine) {
		this.codordine = codordine;
	}

	public void setDataAcquisto(Date datacquisto) {
		this.datacquisto = datacquisto;
	}

	public void setIdCliente(String idct) {
		this.idct = idct;
	}

	public void setIdDipendente(String iddip) {
		this.iddip = iddip;
	}

	public void setPrezzoTotale(double prezzotot) {
		this.prezzotot = prezzotot;
	}

	@Override
	public String toString() {
		return codordine + " " + datacquisto + " " + prezzotot + " " + idct + " " + iddip;
	}
}
