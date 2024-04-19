package com.pellegrinoprincipe;

public class Time_REV_1 extends Object
{
    // variabili di istanza private
    private int ora;
    private int minuti;
    private int secondi;

    public Time_REV_1() // costruttore
    {
        ora = minuti = secondi = 0;
    }
    // metodo non costruttore che ritorna semplicemente
    // il valore passato al parametro
    public int Time_REV_1(int a)
    {
        return a;
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
    public String toString()
    {
        return "Orario corrente: " + getTime();
    }
}
