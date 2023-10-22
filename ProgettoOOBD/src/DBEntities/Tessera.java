package DBEntities;

public class Tessera {

    private String codiceTessera;
    private int numeroPunti;
    private Cliente intestatario;

    public Tessera(String codiceTessera, int numeroPunti, Cliente intestatario) {
        this.codiceTessera = codiceTessera;
        this.numeroPunti = numeroPunti;
        this.intestatario = intestatario;
    }

    public String getCodiceTessera() {
        return codiceTessera;
    }

    public void setCodiceTessera(String codiceTessera) {
        this.codiceTessera = codiceTessera;
    }

    public int getNumeroPunti() {
        return numeroPunti;
    }

    public void setNumeroPunti(int numeroPunti) {
        this.numeroPunti = numeroPunti;
    }

    public Cliente getIntestatario() {
        return intestatario;
    }

    public void setIntestatario(Cliente intestatario) {
        this.intestatario = intestatario;
    }
}
