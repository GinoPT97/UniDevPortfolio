package com.pellegrinoprincipe;

public class EnFor
{
    public static void main(String[] args)
    {
        int a[] = {1, 200, 400, 500};
        int sum = 0;

        for (int elem : a) // for avanzato
            sum += elem;

        System.out.println("La somma e' " + sum);
    }
}
