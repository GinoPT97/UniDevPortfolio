package com.pellegrinoprincipe;

public class DoWhile
{
    public static void main(String[] args)
    {
        int a = 8;
        
        System.out.print("a = ");
        do
        {
            System.out.print(a-- + " ");
        }
        while (a >= 0); // finché a >= 0 cicla
    }
}