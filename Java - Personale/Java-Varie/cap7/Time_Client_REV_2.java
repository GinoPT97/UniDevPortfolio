package com.pellegrinoprincipe;

public class Time_Client_REV_2
{
    public static void main(String[] args)
    {
        Time_REV_2 t = new Time_REV_2();

        t.setTime(14, 30, 56);
        // ottengo singolarmente le ore i minuti e i secondi
        System.out.println("Ora: " + t.getOra() + " Minuti: " + t.getMinuti()
                + " Secondi: " + t.getSecondi());
    }
}

