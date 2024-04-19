package com.pellegrinoprincipe;

class StaticClass
{
    static int a = 12;
    static int b;
    final static String msg;

    static void foo(int x)
    {
        System.out.print("{x = " + x + " a = " + a + " a/b = " + (a / b) + "} ");
    }

    static // blocco static
    {
        msg = "Inizializzazione: ";
        System.out.print(msg);
        b = 4;
    }
}

public class StaticBlock
{
    public static void main(String[] args)
    {
        StaticClass.foo(42);
        StaticClass.foo(45);
    }
}
