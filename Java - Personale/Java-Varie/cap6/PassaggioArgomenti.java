package com.pellegrinoprincipe;

class MyInt
{
    public int val;
}

public class PassaggioArgomenti
{
    public static void fooNonMod(int a_in_method)
    {
        a_in_method = 100; // qui a_in_method non modifica il valore dell'argomento
    }

    public static void fooMod(MyInt a_in_method_rif)
    {
        a_in_method_rif.val = 100; // qui a_in_method_rif modifica il valore dell'argomento
    }

    public static void main(String[] args)
    {
        MyInt an_int = new MyInt(); // oggetto di tipo MyInt
        an_int.val = 200; // qui vale 200

        fooMod(an_int); // invoco fooMod 
        System.out.println("an_int.val vale: " + an_int.val); // qui vale 100

        int c = 200;
        fooNonMod(c); // invoco fooNonMod
        System.out.println("c vale: " + c); // qui vale ancora 200
    }
}
