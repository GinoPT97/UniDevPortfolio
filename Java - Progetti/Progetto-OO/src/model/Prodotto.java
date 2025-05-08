package model;

import java.util.Date;

public class Prodotto {
	private String codprodotto;
	private String nome;
	private String descrizione;
	private String luogoprov;
	private double prezzo;
	private Date datamungitura;
	private Date datascadenza;
	private Date dataraccolta;
	private boolean glutine;
	private String categoria;
	private int scorta;

	public Prodotto(String codprodotto, String nome, String descrizione, double prezzo, String luogoprov,
			Date dataraccolta, Date datamungitura, boolean glutine, Date datascadenza, String categoria, int scorta) {
		this.codprodotto = codprodotto;
		this.nome = nome;
		this.descrizione = descrizione;
		this.luogoprov = luogoprov;
		this.prezzo = prezzo;
		this.glutine = glutine;
		this.datamungitura = datamungitura;
		this.dataraccolta = dataraccolta;
		this.datascadenza = datascadenza;
		this.categoria = categoria;
		this.scorta = scorta;
	}

	public String getCodProd() {
		return codprodotto;
	}

	public void setCodProd(String codprodotto) {
		this.codprodotto = codprodotto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getLuogoProv() {
		return luogoprov;
	}

	public void setLuogoProv(String luogoprov) {
		this.luogoprov = luogoprov;
	}

	public Date getDatamungitura() {
		return datamungitura;
	}

	public void setDatamungitura(Date datamungitura) {
		this.datamungitura = datamungitura;
	}

	public Date getDatascadenza() {
		return datascadenza;
	}

	public void setDatascadenza(Date datascadenza) {
		this.datascadenza = datascadenza;
	}

	public Date getDataraccolta() {
		return dataraccolta;
	}

	public void setDataraccolta(Date dataraccolta) {
		this.dataraccolta = dataraccolta;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public boolean isGlutine() {
		return glutine;
	}

	public void setGlutine(boolean glutine) {
		this.glutine = glutine;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getScorta() {
		return scorta;
	}

	public void setScorta(int scorta) {
		this.scorta = scorta;
	}

	@Override
	public String toString() {
		return codprodotto + " " + nome + " " + descrizione + " " + prezzo + " " + luogoprov + " " + dataraccolta + " "
				+ datamungitura + " " + glutine + " " + datascadenza + " " + categoria + " " + scorta + " ";
	}
}
