package com.pellegrinoprincipe;

public class Time extends Object
{
    // variabili di istanza private
    private int ora;
    private int minuti;
    private int secondi;

    public Time() // costruttore
    {
        ora = minuti = secondi = 0;
    }
    public void setTime(int o, int m, int s) // metodo per impostare un tempo
    {
        ora = (o < 24 && o >= 0) ? o : 0;
        minuti = (m < 60 && m >= 0) ? m : 0;
        secondi = (s < 60 && s >= 0) ? s : 0;
    }
    public String getTime() // metodo per ottenere un tempo
    {
        return ora + ":" + minuti + ":" + secondi;
    }
    public String toString() // stampa una rappresentazione leggibile di un oggetto Time
    {
        return "Orario corrente: " + getTime();
    }
}
