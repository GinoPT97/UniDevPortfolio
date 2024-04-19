package com.pellegrinoprincipe;

public class Time_Client
{
    public static void main(String[] args)
    {
        Time t = new Time(); // istanza di Time

        System.out.println("Time con i valori di default: " + t.getTime());
        t.setTime(14, 30, 56); // imposto nuovi valori per un tempo
        System.out.println("Time con i valori impostati: " + t);
    }
}
