package com.pellegrinoprincipe;

public class Break
{
    public static void main(String[] args)
    {
        for (int a = 1; a <= 10; a++) // cicla finché a <= 10
        {
            if (a == 5)
            {
                break;
            }
            System.out.print(a + " ");
        }
        System.out.println();

        int a = 1;

        while (a <= 10) // cicla finché a <= 10
        {
            if (a == 5)
                break;
            System.out.print(a++ + " ");
        }
    }
}