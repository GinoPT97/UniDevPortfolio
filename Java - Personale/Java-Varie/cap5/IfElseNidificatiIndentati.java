package com.pellegrinoprincipe;

public class IfElseNidificatiIndentati
{
    public static void main(String[] args)
    {
        int a = 3;

        if (a >= 10)
            System.out.println("a >= 10"); // eseguita se a è maggiore o uguale di 10
        else if (a >= 5)
            System.out.println("a >= 5 e a < 10");
        else if (a >= 0)
            System.out.println("a >= 0 e a < 5");
    }
}