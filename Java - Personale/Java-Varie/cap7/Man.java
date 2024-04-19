package com.pellegrinoprincipe;

public class Man
{
    private String cognome;
    private String nome;
    private Time_REV_5 time_to_work; // oggetto di tipo Time_REV_5

    public Man(String c, String n, int o)
    {
        cognome = c;
        nome = n;
        time_to_work = new Time_REV_5(o); // impostazione dell'orario
    }
    public String toString()
    {
        return cognome + " " + nome + " va a lavorare alle ore: " + time_to_work.getOra();
    }
}
