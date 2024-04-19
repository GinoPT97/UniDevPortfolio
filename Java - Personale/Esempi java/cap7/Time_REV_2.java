package com.pellegrinoprincipe;

public class Time_REV_2 extends Object
{
    // variabili di istanza private
    private int ora;
    private int minuti;
    private int secondi;

    // costruttore
    public Time_REV_2()
    {
        ora = minuti = secondi = 0;
    }

    public int Time_REV_2(int a)
    {
        return a;
    }

    public void setTime(int o, int m, int s)
    {
        ora = (o < 24 && o >= 0) ? o : 0;
        minuti = (m < 60 && m >= 0) ? m : 0;
        secondi = (s < 60 && s >= 0) ? s : 0;
    }

    // ottengo come stringa  il tempo con la separazione di
    // ore, minuti e secondi dal carattere :
    public String getTime()
    {
        return ora + ":" + minuti + ":" + secondi;
    }

    // ottengo l'ora
    public int getOra()
    {
        return ora;
    }

    // ottengo i minuti
    public int getMinuti()
    {
        return minuti;
    }

    // ottengo i secondi
    public int getSecondi()
    {
        return secondi;
    }

    // toString dell'oggetto Time_REV_2
    public String toString()
    {
        return "Orario corrente: " + getTime();
    }
}
