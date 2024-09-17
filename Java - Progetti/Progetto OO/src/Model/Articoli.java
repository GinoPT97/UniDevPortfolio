package Model;

public class Articoli {
	private String codOrdine;
	private String codProdotto;
	private double prezzo;
	private double numPunti;
	private int numeroArticoli;
	private String categoria;

	public Articoli(String codOrdine, String codProdotto, double prezzo, double numPunti, int numeroArticoli,
			String categoria) {
		this.codOrdine = codOrdine;
		this.codProdotto = codProdotto;
		this.prezzo = prezzo;
		this.numPunti = numPunti;
		this.numeroArticoli = numeroArticoli;
		this.categoria = categoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getCodOrdine() {
		return codOrdine;
	}

	public String getCodProdotto() {
		return codProdotto;
	}

	public int getNumeroArticoli() {
		return numeroArticoli;
	}

	public double getNumPunti() {
		return numPunti;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setCodOrdine(String codOrdine) {
		this.codOrdine = codOrdine;
	}

	public void setCodProdotto(String codProdotto) {
		this.codProdotto = codProdotto;
	}

	public void setNumeroArticoli(int numeroArticoli) {
		this.numeroArticoli = numeroArticoli;
	}

	public void setNumPunti(double numPunti) {
		this.numPunti = numPunti;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	@Override
	public String toString() {
		return codOrdine + " " + codProdotto + " " + prezzo + " " + numPunti + " " + numeroArticoli + " " + categoria;
	}
}
