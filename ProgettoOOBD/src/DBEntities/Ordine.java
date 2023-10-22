package DBEntities;

import java.util.*;

public class Ordine {

    private String codice;
    private Cliente acquirente;
    private Dipendente venditore;
    private Date dataAcquisto;
    private float prezzoTotale;
    private ArrayList<Prodotto> articoliAcquistati = new ArrayList<Prodotto>();

    public Ordine(String codice, Cliente acquirente, Dipendente venditore, Date dataAcquisto, float prezzoTotale, ArrayList<Prodotto> articoliAcquistati) {
        this.codice = codice;
        this.acquirente = acquirente;
        this.venditore = venditore;
        this.dataAcquisto = dataAcquisto;
        this.prezzoTotale = prezzoTotale;
        this.articoliAcquistati = articoliAcquistati;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Cliente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Cliente acquirente) {
        this.acquirente = acquirente;
    }

    public Dipendente getVenditore() {
        return venditore;
    }

    public void setVenditore(Dipendente venditore) {
        this.venditore = venditore;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(Date dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    public float getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(float prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public ArrayList<Prodotto> getArticoliAcquistati() {
        return articoliAcquistati;
    }

    public void setArticoliAcquistati(ArrayList<Prodotto> articoliAcquistati) {
        this.articoliAcquistati = articoliAcquistati;
    }

}
