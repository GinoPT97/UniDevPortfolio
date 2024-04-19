package com.pellegrinoprincipe;

public class BlocchiAnnidati
{
    public static void main(String[] args)
    {
        int x = 20;
        if (x < 20)
        {
            int y = 11;
            System.out.println("X " + x);
        }
        System.out.println("Y " + y); // errore 'y' non è visibile
    }
}
