package com.pellegrinoprincipe;

public abstract class Employee
{
    private String nome;
    private String cognome;

    public Employee(String n, String c)
    {
        nome = n;
        cognome = c;
    }

    protected String getNome() { return nome; }
    protected String getCognome() { return cognome; }

    public String toString()
    {
        return cognome + " " + nome;
    }
    public abstract int earning(); // metodo astratto
}
