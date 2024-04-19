package com.pellegrinoprincipe;

public class Engineer extends Employee
{
    private int percentage;
    private int fisso;

    public Engineer(String n, String c, int p, int f)
    {
        super(n, c);
        setPercentage(p);
        setFisso(f);
    }

    public void setFisso(int f) // imposto il fisso come paga
    {
        fisso = f > 0 ? f : 0;
    }   
    public void setPercentage(int p) // imposto la percentuale
    {
        percentage = p > 0 ? p : 0;
    }
    public int earning() // calcolo specializzato del guadagno
    {
        return fisso + (fisso * percentage / 100);
    }
    public String toString()
    {
        return super.toString() + " guadagna € ";
    }
}
