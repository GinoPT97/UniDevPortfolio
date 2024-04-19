
package com.pellegrinoprincipe;

public class Time_REV_4
{
    // variabili di istanza private
    private int ora;
    private int minuti;
    private int secondi;

    // costruttore
    public Time_REV_4()
    {
        setTime(0, 0, 0);
    }

    // costruttore che inizializza solo l'ora
    public Time_REV_4(int h)
    {
        setTime(h, 0, 0);
    }

    // costruttore che inizializza ora e minuti
    public Time_REV_4(int h, int m)
    {
        setTime(h, m, 0);
    }

    // costruttore che inizializza ora, minuti e secondi
    public Time_REV_4(int h, int m, int s)
    {
        setTime(h, m, s);
    }

    // costruttore che inizializza un oggetto Time_REV_4 attraverso
    // un altro oggetto Time_REV_4
    public Time_REV_4(Time_REV_4 t)
    {
        setTime(t.ora, t.minuti, t.secondi);
    }

    public void setTime(int o, int m, int s)
    {
        setOra(o);
        setMinuti(m);
        setSecondi(s);
    }

    // imposto il campo ora
    public void setOra(int o)
    {
        ora = (o < 24 && o >= 0) ? o : 0;
    }

    // imposto il campo minuti
    public void setMinuti(int m)
    {
        minuti = (m < 60 && m >= 0) ? m : 0;
    }

    // imposto il campo secondi
    public void setSecondi(int s)
    {
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
