package DBEntities;

import java.util.ArrayList;

public class Dipendente {

    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String indirizzo;
    //private int[] telefono = new int[2];
    private String telefono;
    private String email;
    private String codiceDipendente;
    private ArrayList<Ordine> ordini = new ArrayList<Ordine>();

    public Dipendente(String codiceDipendente, String nome, String cognome, String codiceFiscale, String indirizzo, String telefono, String email) { //, ArrayList<Ordine> ordini) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.codiceDipendente = codiceDipendente;
        //this.ordini = ordini;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodiceDipendente() {
        return codiceDipendente;
    }

    public void setCodiceDipendente(String codiceDipendente) {
        this.codiceDipendente = codiceDipendente;
    }

    public ArrayList<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(ArrayList<Ordine> ordini) {
        this.ordini = ordini;
    }
}
