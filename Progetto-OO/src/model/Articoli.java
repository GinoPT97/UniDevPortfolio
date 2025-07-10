package model;

public class Articoli {
    private String codOrdine;
    private String codProdotto;
    private double prezzo;
    private int numeroArticoli;
    private int codCliente;

    // Costruttore unico
    public Articoli(String codOrdine, String codProdotto, double prezzo, int numeroArticoli, int codCliente) {
        this.codOrdine = codOrdine;
        this.codProdotto = codProdotto;
        this.prezzo = prezzo;
        this.numeroArticoli = numeroArticoli;
        this.codCliente = codCliente;
    }

    public String getCodOrdine() {
        return codOrdine;
    }

    public void setCodOrdine(String codOrdine) {
        this.codOrdine = codOrdine;
    }

    public String getCodProdotto() {
        return codProdotto;
    }

    public void setCodProdotto(String codProdotto) {
        this.codProdotto = codProdotto;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getNumeroArticoli() {
        return numeroArticoli;
    }

    public void setNumeroArticoli(int numeroArticoli) {
        this.numeroArticoli = numeroArticoli;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    @Override
    public String toString() {
        return codOrdine + " " + codProdotto + " " + prezzo + " " + numeroArticoli + " " + codCliente;
    }
}
