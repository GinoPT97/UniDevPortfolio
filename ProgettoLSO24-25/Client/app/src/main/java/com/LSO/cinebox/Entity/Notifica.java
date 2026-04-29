package com.LSO.cinebox.Entity;

public class Notifica {
    private int idNotifica;
    private int idUtente;
    private String messaggio;
    private String dataNotifica;
    private String stato;

    public Notifica() {
    }

    public int getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getDataNotifica() {
        return dataNotifica;
    }

    public void setDataNotifica(String dataNotifica) {
        this.dataNotifica = dataNotifica;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
