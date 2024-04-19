package com.pellegrinoprincipe;

public class Laborer extends Employee
{
    private int percentage[] = {2, 5, 8, 11};
    private int ora_paga = 8;
    private int ore_lavorate;
    private int pezzi;

    public Laborer(String n, String c, int p, int o)
    {
        super(n, c);

        setPezzi(p);
        setOreLavorate(o);
    }

    public void setOreLavorate(int o) // imposto le ore lavorate
    {
        ore_lavorate = o > 0 ? o : 0;
    }
    public void setPezzi(int p) // imposto i pezzi da lavorare
    {
        pezzi = p >= 0 && p <= 3 ? p : -1;
    }
    public int earning() // specializzazione del calcolo della paga
      {
        int p = 0;
        if (ore_lavorate > 0)
        {
            p = ore_lavorate * ora_paga;
            if (pezzi != -1)
                p += (p * percentage[pezzi] / 100);
            return p;
        }
        else
            return 0;
    }
    public String toString()
    {
        return super.toString() + " guadagna € ";
    }
}
