package com.pellegrinoprincipe;

public class ElsePendenteCorretto
{
    public static void main(String[] args)
    {
        int a = 9, b = 3;

        if (a > 10)
        {
            if (b > 10)
                System.out.println("a e b > 10"); // eseguita se a e b sono maggiori di 10
        }
        else
            System.out.println("a < 10");
    }
}