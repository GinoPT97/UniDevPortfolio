package model;

import java.sql.Date;

public class Ordine {
	private String codordine;
	private Date datacquisto;
	private double prezzotot;
	private int idct;
	private int iddip;

	// Costruttore unico
	public Ordine(String codordine, Date datacquisto, double prezzotot, int idct, int iddip) {
		this.codordine = codordine;
		this.datacquisto = datacquisto;
		this.prezzotot = prezzotot;
		this.idct = idct;
		this.iddip = iddip;
	}

	public String getCodOrdine() {
		return codordine;
	}

	public void setCodOrdine(String codordine) {
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

	public int getIdCliente() {
		return idct;
	}

	public void setIdCliente(int idct) {
		this.idct = idct;
	}

	public int getIdDipendente() {
		return iddip;
	}

	public void setIdDipendente(int iddip) {
		this.iddip = iddip;
	}

	@Override
	public String toString() {
		return codordine + " " + datacquisto + " " + prezzotot + " " + idct + " " + iddip;
	}
}
