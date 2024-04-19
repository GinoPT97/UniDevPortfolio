package com.pellegrinoprincipe;

public class Man_REV_1
{
    private String cognome;
    private String nome;
    private Time_REV_5 time_to_work;
    public static int how_many; // membro di classe

    public Man_REV_1(String c, String n, int o)
    {
        cognome = c;
        nome = n;
        time_to_work = new Time_REV_5(o);
        how_many++; // incrementala per sapere quanti oggetti di tipo  Man_REV_1 sono stati creati
    }

    public String toString()
    {
        return cognome + " " + nome + " va a lavorare alle ore: " + time_to_work.getOra();
    }
}
