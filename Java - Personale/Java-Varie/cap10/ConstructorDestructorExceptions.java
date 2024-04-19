package com.pellegrinoprincipe;

class Test
{
    private double d[][] = new double[1000000][];

    public Test() // costruttore
    {
        try
        {
            for (int a = 0; a < d.length; a++) // genero un'eccezione ...troppa RAM usata...
                d[a] = new double[8000000];
        }
        catch (OutOfMemoryError oom)
        {
            System.out.println("Eccezione dal *COSTRUTTORE* DI Test: basta, ho finto la RAM...");
        }
    }
    public void finalize() // distruttore
    {
        int r = 0;
        try
        {
            r = 10 / 0; // divisione per 0   
        }
        catch (ArithmeticException ae)
        {
            System.out.println("Eccezione dal *DISTRUTTORE* di Test: Divisione per 0.");
        }
    }
}

public class ConstructorDestructorExceptions
{
    public static void main(String args[])
    {
        Test t = new Test();
        t = null; // t non punta più a nulla
        System.gc(); // forza l'esecuzione del Garbage Collector
     }
}
