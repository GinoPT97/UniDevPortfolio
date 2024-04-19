package com.pellegrinoprincipe;

import java.util.ArrayList;

class SumArrayGenericWildcard
{
    public SumArrayGenericWildcard() {}

    // calcola la somma di ArrayList di tipi numerici derivati da Number
    public double sum(ArrayList<? extends Number> al)
    {
        double total = 0;
        for (Number nr : al)
            total += nr.doubleValue();
        return total;
    }
}

public class SumArrayGenericWildcardClient
{
    public static void main(String[] args)
    {
        SumArrayGenericWildcard sgw = new SumArrayGenericWildcard();

        Double d[] = { 11.1, 11.2 };
        Integer i[] = { 12, 13 };

        ArrayList<Integer> ali = new ArrayList<>(); // ArrayList di interi
        for (Integer i_e : i)
            ali.add(i_e);

        ArrayList<Double> ald = new ArrayList<>(); // ArrayList di double
        for (Double d_e : d)
            ald.add(d_e);

        // mostriamo la somma
        System.out.print("ArrayList<Integer> somma: " + sgw.sum(ali));
        System.out.println(" | ArrayList<Double> somma: " + sgw.sum(ald));
    }
}

