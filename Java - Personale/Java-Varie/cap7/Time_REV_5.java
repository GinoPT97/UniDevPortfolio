package com.pellegrinoprincipe;

public class Time_REV_5
{
    // membri dato privati
    private int ora;
    private int minuti;
    private int secondi;
    private final String MSG = "Ora assegnata = ";

    // costruttore
    public Time_REV_5()
    {
        setTime(0, 0, 0);
    }

    public Time_REV_5(int h)
    {
        setTime(h, 0, 0);
    }

    public Time_REV_5(int h, int m)
    {
        setTime(h, m, 0);
    }

    public Time_REV_5(int h, int m, int s)
    {
        setTime(h, m, s);
    }

    public Time_REV_5(Time_REV_5 t)
    {
        setTime(t.ora, t.minuti, t.secondi);
    }

    public void setTime(int o, int m, int s)
    {
        setOra(o);
        setMinuti(m);
        setSecondi(s);
    }

    public void setOra(int o)
    {
        ora = (o < 24 && o >= 0) ? o : 0;
    }

    public void setMinuti(int m)
    {
        minuti = (m < 60 && m >= 0) ? m : 0;
    }

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
        return MSG + getTime();
    }
}
