package com.pellegrinoprincipe;

public class ArgomentoVariabile
{
    public static void doSum(int... c)
    {
        int sum = 0;
        for (int i : c)
            sum += i;

        System.out.println("La somma e': " + sum);
    }

    public static void main(String[] args)
    {
        int one[] = {22, 33, 55};
        int two = 111, three = 444;

        doSum(one); // passo un array
        doSum(two); // passo una sola variabile
        doSum(two, three); // passo due variabili
    }
}
