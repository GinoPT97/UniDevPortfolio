package com.pellegrinoprincipe;

public class Time_REV_3
{
    // variabili di istanza private
    private int ora;
    private int minuti;
    private int secondi;

    // costruttore senza argomenti
    public Time_REV_3()
    {
        setTime(0, 0, 0);
    }
    // costruttore che inizializza solo l'ora
    public Time_REV_3(int h)
    {
        setTime(h, 0, 0);
    }

    // costruttore che inizializza ora e minuti
    public Time_REV_3(int h, int m)
    {
        setTime(h, m, 0);
    }

    // costruttore che inizializza ora, minuti e secondi
    public Time_REV_3(int h, int m, int s)
    {
        setTime(h, m, s);
    }

    // costruttore che inizializza un oggetto Time_REV_3 attraverso
    // un altro oggetto Time_REV_3
    public Time_REV_3(Time_REV_3 t)
    {
        setTime(t.ora, t.minuti, t.secondi);
    }

    public void setTime(int o, int m, int s)
    {
        ora = (o < 24 && o >= 0) ? o : 0;
        minuti = (m < 60 && m >= 0) ? m : 0;
        secondi = (s < 60 && s >= 0) ? s : 0;
    }

    public String getTime()
    {
        return ora + ":" + minuti + ":" + secondi;
    }

    public int getOra()
    {
        return ora;
    }

    public int getMinuti()
    {
        return minuti;
    }

    public int getSecondi()
    {
        return secondi;
    }

    public String toString()
    {
        return "Orario corrente: " + getTime();
    }
}
