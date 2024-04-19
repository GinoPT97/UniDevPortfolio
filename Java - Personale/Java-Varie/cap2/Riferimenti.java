package com.pellegrinoprincipe;

class T
{
}

public class Riferimenti
{
    public static void main(String[] args)
    {
	int x[] = new int[2];
	System.out.println("Valore riferimento dell'array x: " + x);
	T t = new T();
	System.out.println("Valore riferimento dell'oggetto t: " + t);
    }
}
