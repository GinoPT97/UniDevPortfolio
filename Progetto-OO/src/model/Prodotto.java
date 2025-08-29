package model;

import java.time.LocalDate;

public class Prodotto {
	private String codprodotto;
	private String nome;
	private String descrizione;
	private String luogoprov;
	private double prezzo;
	private LocalDate datamungitura;
	private LocalDate datascadenza;
	private LocalDate dataraccolta;
	private LocalDate dataproduzione;
	private boolean glutine;
	private String categoria;
	private int scorta;

	// Costruttore unico
	public Prodotto(String codprodotto, String nome, String descrizione, double prezzo, String luogoprov,
			LocalDate dataraccolta, LocalDate datamungitura, boolean glutine, LocalDate datascadenza, String categoria,
			int scorta, LocalDate dataproduzione) {
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
		this.dataproduzione = dataproduzione;
	}

	public String getCodProdotto() {
		return codprodotto;
	}

	public void setCodProdotto(String codprodotto) {
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

	public String getLuogoProvenienza() {
		return luogoprov;
	}

	public void setLuogoProvenienza(String luogoprov) {
		this.luogoprov = luogoprov;
	}

	public LocalDate getDatamungitura() {
		return datamungitura;
	}

	public void setDatamungitura(LocalDate datamungitura) {
		this.datamungitura = datamungitura;
	}

	public LocalDate getDatascadenza() {
		return datascadenza;
	}

	public void setDatascadenza(LocalDate datascadenza) {
		this.datascadenza = datascadenza;
	}

	public LocalDate getDataraccolta() {
		return dataraccolta;
	}

	public void setDataraccolta(LocalDate dataraccolta) {
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

	public LocalDate getDataProduzione() {
		return dataproduzione;
	}

	public void setDataProduzione(LocalDate dataproduzione) {
		this.dataproduzione = dataproduzione;
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
				+ datamungitura + " " + glutine + " " + datascadenza + " " + dataproduzione + " " + categoria + " "
				+ scorta + " ";
	}
}
