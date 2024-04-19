package com.pellegrinoprincipe;

public class Time_Client_Private_Access
{
    public static void main(String[] args)
    {
        Time t = new Time();
        t.setTime(14, 30, 56);
        t.ora = 15; // qui ERRORE ora è private !!!
    }
}
