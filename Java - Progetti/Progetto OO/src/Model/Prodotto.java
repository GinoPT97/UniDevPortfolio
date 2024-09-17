package Model;

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

	public String getCategoria() {
		return categoria;
	}

	public String getCodProd() {
		return codprodotto;
	}

	public Date getDatamungitura() {
		return datamungitura;
	}

	public Date getDataraccolta() {
		return dataraccolta;
	}

	public Date getDatascadenza() {
		return datascadenza;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getLuogoProv() {
		return luogoprov;
	}

	public String getNome() {
		return nome;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public int getScorta() {
		return scorta;
	}

	public boolean isGlutine() {
		return glutine;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setCodProd(String codprodotto) {
		this.codprodotto = codprodotto;
	}

	public void setDatamungitura(Date datamungitura) {
		this.datamungitura = datamungitura;
	}

	public void setDataraccolta(Date dataraccolta) {
		this.dataraccolta = dataraccolta;
	}

	public void setDatascadenza(Date datascadenza) {
		this.datascadenza = datascadenza;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setGlutine(boolean glutine) {
		this.glutine = glutine;
	}

	public void setLuogoProv(String luogoprov) {
		this.luogoprov = luogoprov;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
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
