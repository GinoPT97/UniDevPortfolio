package com.pellegrinoprincipe;

public class ArrayMono
{
    public static void main(String[] args)
    {
        char name[] = {'P', 'e', 'l', 'l', 'e', 'g', 'r', 'i', 'n', 'o'}; // array di caratteri
        int div = 4;
        for (int i = 0; i < name.length; i++)
        {
            if (i <= div)
                System.out.print(name[i] + " ");
            else
                System.out.print("\n" + name[i]);
        }
    }
}
