package com.pellegrinoprincipe;

public class ForExpr
{
    public static void main(String[] args)
    {
        int var1 = 3, var2 = 2;
 
        System.out.println("a\tz");
        for (int a = var1 * 2 + var2, z = 0; a >= 0; a--, z++)
            System.out.println(a + "\t" + z);
    }
}