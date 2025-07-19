
package model;

import java.time.LocalDate;

public class Tessera {
    private String codtessera;
    private double numeropunti;
    private LocalDate dataemissione;
    private LocalDate datascadenza;
    private String stato;
    private Cliente proprietario;

    // Costruttore unico
    public Tessera(String codtessera, double numeropunti, LocalDate dataemissione, LocalDate datascadenza, String stato, Cliente proprietario) {
        this.codtessera = codtessera;
        this.numeropunti = numeropunti;
        this.dataemissione = dataemissione;
        this.datascadenza = datascadenza;
        this.stato = stato;
        this.proprietario = proprietario;
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

    public LocalDate getDataEmissione() {
        return dataemissione;
    }

    public void setDataEmissione(LocalDate dataemissione) {
        this.dataemissione = dataemissione;
    }

    public LocalDate getDataScadenza() {
        return datascadenza;
    }

    public void setDataScadenza(LocalDate datascadenza) {
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
