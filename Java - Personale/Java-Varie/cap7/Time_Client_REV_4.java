package com.pellegrinoprincipe;

public class Time_Client_REV_4
{
    public static void main(String[] args)
    {
        Time_REV_4 t = new Time_REV_4();

        // imposta singolarmente ora, minuti e secondi
        t.setOra(18);
        t.setMinuti(30);
        t.setSecondi(25);
        System.out.println("t = " + t.getOra() + ":" + t.getMinuti() + ":" + t.getSecondi());
    }
}
