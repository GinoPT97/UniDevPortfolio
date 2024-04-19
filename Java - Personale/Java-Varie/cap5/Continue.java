package com.pellegrinoprincipe;

public class Continue
{
    public static void main(String[] args)
    {
        for (int a = 1; a <= 10; a++) // cicla finché a <= 10
        {
            if (a == 5) // salta l'istruzione successiva se a == 5
                continue;
            System.out.print(a + (a != 10 ? ", " : ""));
        }
        System.out.println();

        int a = 1;

        while (a <= 10) // cicla finché a <= 10
        {
            if (a == 5) // salta l'istruzione successiva se a == 5    
            {
                a++;
                continue;
            }
            System.out.print(a + (a != 10 ? ", " : ""));
            a++;
        }
    }
}