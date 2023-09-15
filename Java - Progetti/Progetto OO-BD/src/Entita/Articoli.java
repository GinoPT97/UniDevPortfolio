package Entita;

public class Articoli {
    private String codOrdine;
    private String codProdotto;
    private double prezzo;
    private double numPunti;
    private int numeroArticoli;
    private String categoria;
    
    public Articoli(String codOrdine, String codProdotto, double prezzo, double numPunti, int numeroArticoli, String categoria) {
        this.codOrdine = codOrdine;
        this.codProdotto = codProdotto;
        this.prezzo = prezzo;
        this.numPunti = numPunti;
        this.numeroArticoli = numeroArticoli;
        this.categoria = categoria;
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
    
    public double getNumPunti() {
        return numPunti;
    }
    
    public void setNumPunti(double numPunti) {
        this.numPunti = numPunti;
    }
    
    public int getNumeroArticoli() {
        return numeroArticoli;
    }
    
    public void setNumeroArticoli(int numeroArticoli) {
        this.numeroArticoli = numeroArticoli;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String toString() {
        return codOrdine + " " + codProdotto + " " + prezzo + " " + numPunti + " " + numeroArticoli + " " + categoria;
    }
}
