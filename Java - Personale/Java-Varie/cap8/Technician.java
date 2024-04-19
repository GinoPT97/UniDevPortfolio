package com.pellegrinoprincipe;

public class Technician extends Employee
{
    private int quantum = 5;
    private int pezzi;
    private int fisso;

    public Technician(String n, String c, int f, int p)
    {
        super(n, c);
        setFisso(f);
        setPezzi(p);
    }

    public void setFisso(int f) // imposto il fisso della paga
    {
        fisso = f > 0 ? f : 0;
    }
    public void setPezzi(int p) // pezzi da lavorare
    {
        pezzi = p > 0 ? p : 0;
    }
    public int earning() // specializzazione della paga
    {
        return fisso + (quantum * pezzi);
    }

    public String toString()
    {
        return super.toString() + " guadagna € ";
    }
}
