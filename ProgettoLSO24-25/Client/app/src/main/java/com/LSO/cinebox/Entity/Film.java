package com.LSO.cinebox.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Film implements Serializable {
    private final int idFilm;
    private final String titolo;
    private final String genere;
    private final String descrizione;
    private final String linguaOriginale;
    private final String dataRilascio;
    private final String locandina;
    private final double votoMedio;
    private final int numeroVoti;
    private final double popolarita;
    private final double prezzo;
    private int numeroCopieDisponibili;
    private int numeroCopieInPrestito;
    private final String stato;

    public Film(int idFilm, String titolo, String genere, String descrizione, String linguaOriginale, String dataRilascio, String locandina,
                double votoMedio, int numeroVoti, double popolarita, double prezzo, int numeroCopieDisponibili, int numeroCopieInPrestito, String stato) {
        this.idFilm = idFilm;
        this.titolo = titolo;
        this.genere = genere;
        this.descrizione = descrizione;
        this.linguaOriginale = linguaOriginale;
        this.dataRilascio = dataRilascio;
        this.locandina = locandina;
        this.votoMedio = votoMedio;
        this.numeroVoti = numeroVoti;
        this.popolarita = popolarita;
        this.prezzo = prezzo;
        this.numeroCopieDisponibili = numeroCopieDisponibili;
        this.numeroCopieInPrestito = numeroCopieInPrestito;
        this.stato = stato;
    }

    public static List<Film> parseFilmsFromRawData(String rawData) {
        List<Film> films = new ArrayList<>();
        try {
            String[] filmDataArray = rawData.split("\n");
            for (String filmData : filmDataArray) {
                filmData = filmData.trim();
                if (filmData.isEmpty() || filmData.equals("END")) continue;
                String[] tokens = filmData.split("\t");
                if (tokens.length == 14) {
                    int idFilm = Integer.parseInt(tokens[0]);
                    String titolo = tokens[1];
                    String genere = tokens[2];
                    String descrizione = tokens[3];
                    String linguaOriginale = tokens[4];
                    String dataRilascio = tokens[5];
                    String locandina = tokens[6];
                    double votoMedio = Double.parseDouble(tokens[7]);
                    int numeroVoti = Integer.parseInt(tokens[8]);
                    double popolarita = Double.parseDouble(tokens[9]);
                    double prezzo = Double.parseDouble(tokens[10]);
                    int numeroCopieDisponibili = Integer.parseInt(tokens[11]);
                    int numeroCopieInPrestito = Integer.parseInt(tokens[12]);
                    String stato = tokens[13];
                    films.add(new Film(idFilm, titolo, genere, descrizione, linguaOriginale, dataRilascio, locandina,
                            votoMedio, numeroVoti, popolarita, prezzo, numeroCopieDisponibili, numeroCopieInPrestito, stato));
                }
            }
        } catch (Exception ignored) {}
        return films;
    }

    public int getIdFilm() { return idFilm; }
    public String getTitolo() { return titolo; }
    public String getGenere() { return genere; }
    public String getDescrizione() { return descrizione; }
    public String getLinguaOriginale() { return linguaOriginale; }
    public String getDataRilascio() { return dataRilascio; }
    public String getLocandina() { return locandina; }
    public double getVotoMedio() { return votoMedio; }
    public int getNumeroVoti() { return numeroVoti; }
    public double getPopolarita() { return popolarita; }
    public double getPrezzo() { return prezzo; }
    public int getNumeroCopieDisponibili() { return numeroCopieDisponibili; }
    public int getNumeroCopieInPrestito() { return numeroCopieInPrestito; }
    public String getStato() { return stato; }
    public String getLocandinaUrl() { return "https://image.tmdb.org/t/p/w500" + locandina; }
    public void setNumeroCopieDisponibili(int numeroCopieDisponibili) { this.numeroCopieDisponibili = numeroCopieDisponibili; }
    public void setNumeroCopieInPrestito(int numeroCopieInPrestito) { this.numeroCopieInPrestito = numeroCopieInPrestito; }
}