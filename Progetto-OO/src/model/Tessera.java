package model;

import java.util.Date;

public class Tessera {
    private String codtessera;
    private double numeropunti;
    private Date dataemissione;
    private Date datascadenza;
    private String stato;
    private Cliente proprietario;

    public Tessera(String codtessera, double numeropunti, Date dataemissione, Date datascadenza, String stato, Cliente proprietario) {
        this.codtessera = codtessera;
        this.numeropunti = numeropunti;
        this.dataemissione = dataemissione;
        this.datascadenza = datascadenza;
        this.stato = stato;
        this.proprietario = proprietario;
    }

    // Costruttore compatibile con il codice esistente
    public Tessera(String codtessera, int npunti, Cliente proprietario) {
        this.codtessera = codtessera;
        this.numeropunti = npunti;
        this.dataemissione = new Date();
        this.datascadenza = new Date(System.currentTimeMillis() + (2L * 365 * 24 * 60 * 60 * 1000)); // 2 anni
        this.stato = "ATTIVA";
        this.proprietario = proprietario;
    }

    // Costruttore per la creazione da ResultSet del database
    public Tessera(String codtessera, double numeropunti, Cliente proprietario, Date dataemissione, Date datascadenza, String stato) {
        this.codtessera = codtessera;
        this.numeropunti = numeropunti;
        this.proprietario = proprietario;
        this.dataemissione = dataemissione;
        this.datascadenza = datascadenza;
        this.stato = stato;
    }

    public String getCodTessera() {
        return codtessera;
    }

    public void setCodTessera(String codtessera) {
        this.codtessera = codtessera;
    }

    public double getNumeroPunti() {
        return numeropunti;
    }

    public void setNumeroPunti(double numeropunti) {
        this.numeropunti = numeropunti;
    }

    // Metodo compatibile con il codice esistente
    public int getNPunti() {
        return (int) numeropunti;
    }

    public void setNPunti(int npunti) {
        this.numeropunti = npunti;
    }

    public Date getDataEmissione() {
        return dataemissione;
    }

    public void setDataEmissione(Date dataemissione) {
        this.dataemissione = dataemissione;
    }

    public Date getDataScadenza() {
        return datascadenza;
    }

    public void setDataScadenza(Date datascadenza) {
        this.datascadenza = datascadenza;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Cliente getProprietario() {
        return proprietario;
    }

    public void setProprietario(Cliente proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public String toString() {
        return codtessera + " " + numeropunti + " " + stato;
    }
}
