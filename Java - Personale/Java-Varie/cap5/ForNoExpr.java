package com.pellegrinoprincipe;

public class ForNoExpr
{
    public static void main(String[] args)
    {
        int x = 8;

        for (;;) // ciclo infinito che è interrotto dal break
        {
            if (x < 0)
                break;
            System.out.print(x-- + " ");
        }
    }
}