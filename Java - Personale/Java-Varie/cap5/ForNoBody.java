package com.pellegrinoprincipe;

public class ForNoBody
{
    public static void main(String[] args)
    {
        int val_max = 100, i = 0;

        for (i = 0; i < val_max; i++) // ciclo senza corpo
            ;

        System.out.println(i); // i vale 100!!!
    }
}