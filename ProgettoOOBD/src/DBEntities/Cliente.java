package DBEntities;

import java.util.ArrayList;

public class Cliente {

    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String indirizzo;
    private int[] telefono = new int[2];
    private String email;
    private String codiceCliente;
    private Tessera tessera;
    private ArrayList<Ordine> ordini = new ArrayList<Ordine>();

    public Cliente(String nome, String cognome, String codiceFiscale, String indirizzo, int[] telefono, String email, String codiceCliente, Tessera tessera, ArrayList<Ordine> ordini) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.email = email;
        this.codiceCliente = codiceCliente;
        this.tessera = tessera;
        this.ordini = ordini;
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

    public void setCodiceFiscale(String codicefiscale) {
        this.codiceFiscale = codicefiscale;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int[] getTelefono() {
        return telefono;
    }

    public void setTelefono(int[] telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodicecliente() {
        return codiceCliente;
    }

    public void setCodicecliente(String codicecliente) {
        this.codiceCliente = codicecliente;
    }

    public ArrayList<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(ArrayList<Ordine> ordini) {
        this.ordini = ordini;
    }

}
