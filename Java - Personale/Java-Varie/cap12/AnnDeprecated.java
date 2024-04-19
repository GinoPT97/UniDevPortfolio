package com.pellegrinoprincipe;

class MyInteger
{
    // metodo deprecato sconsigliato l'utilizzo
    @Deprecated
    public static int doSum(int a, int b) { return a + b; }

    public static int doSum(int[] ii)
    {
        int s = 0;
        for (int i : ii)
            s += i;
        return s;
    }
}

public class AnnDeprecated
{
    public static void main(String[] args)
    {
       MyInteger.doSum(44, 55);
    }
}

