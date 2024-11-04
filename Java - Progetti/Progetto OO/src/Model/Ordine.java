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

	public void setCodOrd(String codordine) {
		this.codordine = codordine;
	}

	public Date getDataAcquisto() {
		return datacquisto;
	}

	public void setDataAcquisto(Date datacquisto) {
		this.datacquisto = datacquisto;
	}

	public double getPrezzoTotale() {
		return prezzotot;
	}

	public void setPrezzoTotale(double prezzotot) {
		this.prezzotot = prezzotot;
	}

	public String getIdCliente() {
		return idct;
	}

	public void setIdCliente(String idct) {
		this.idct = idct;
	}

	public String getIdDipendente() {
		return iddip;
	}

	public void setIdDipendente(String iddip) {
		this.iddip = iddip;
	}

	@Override
	public String toString() {
		return codordine + " " + datacquisto + " " + prezzotot + " " + idct + " " + iddip;
	}
}
