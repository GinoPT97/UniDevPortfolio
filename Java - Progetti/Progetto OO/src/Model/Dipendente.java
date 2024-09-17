package Model;

public class Dipendente {
	private String coddipendente;
	private String nome;
	private String cognome;
	private String codicefiscale;
	private String email;
	private String indirizzo;
	private String telefono;

	public Dipendente(String coddipendente, String nome, String cognome, String codicefiscale, String email,
			String indirizzo, String telefono) {
		this.coddipendente = coddipendente;
		this.nome = nome;
		this.cognome = cognome;
		this.codicefiscale = codicefiscale;
		this.email = email;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
	}

	public String getCodDIP() {
		return coddipendente;
	}

	public String getCodFis() {
		return codicefiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {
		return email;
	}

	public String getInd() {
		return indirizzo;
	}

	public String getNome() {
		return nome;
	}

	public String getTel() {
		return telefono;
	}

	public void setCodDIP(String coddipendente) {
		this.coddipendente = coddipendente;
	}

	public void setCodFis(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setInd(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTel(String telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
		return coddipendente + " " + nome + " " + cognome + " " + codicefiscale + " " + email + " " + indirizzo + " "
				+ telefono + " ";
	}
}
