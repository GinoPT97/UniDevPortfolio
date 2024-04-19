package com.pellegrinoprincipe;

class ExceptionDivideByZero extends ArithmeticException // classe eccezione
{
    private static String msg = "Errore di divisione per 0!";
    public ExceptionDivideByZero() { super(msg); }
}

public class ExceptionDivideByZeroClient
{
    public static void makeDiv(int num, int denom) // metodo per fare la divisione
    {
        if (denom == 0)
            throw new ExceptionDivideByZero();
        else
            System.out.println("Divisione di " + num + " per " + denom + " = " + (num / denom));
    }

    public static void main(String[] args)
    {
        int num = 22;
        int denom[] = {11, 0, 2, 4}; // denominatori

        for (int n = 0; n < denom.length; n++) // fai la divisione...
        {
            try
            {
                makeDiv(num, denom[n]);
            }
            catch (ExceptionDivideByZero e)
            {
                System.out.println(e);
            }
        }
    }
}
