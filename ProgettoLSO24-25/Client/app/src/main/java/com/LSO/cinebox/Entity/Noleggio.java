package com.LSO.cinebox.Entity;

public class Noleggio {
    private int idNoleggio;
    private int idUtente;
    private int idFilm;
    private int numeroCopieNoleggiate;
    private String dataNoleggio;
    private String dataRestituzione;
    private boolean restituito;

    public int getIdNoleggio() {
        return idNoleggio;
    }

    public void setIdNoleggio(int idNoleggio) {
        this.idNoleggio = idNoleggio;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getNumeroCopieNoleggiate() {
        return numeroCopieNoleggiate;
    }

    public void setNumeroCopieNoleggiate(int numeroCopieNoleggiate) {
        this.numeroCopieNoleggiate = numeroCopieNoleggiate;
    }

    public String getDataNoleggio() {
        return dataNoleggio;
    }

    public void setDataNoleggio(String dataNoleggio) {
        this.dataNoleggio = dataNoleggio;
    }

    public String getDataRestituzione() {
        return dataRestituzione;
    }

    public void setDataRestituzione(String dataRestituzione) {
        this.dataRestituzione = dataRestituzione;
    }

    public boolean isRestituito() {
        return restituito;
    }

    public void setRestituito(boolean restituito) {
        this.restituito = restituito;
    }
}
