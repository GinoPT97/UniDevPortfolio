package com.pellegrinoprincipe;

import java.util.Iterator;
import java.util.Random;

class Numbers implements Iterable<Integer>
{
    private int nrs[];

    public Numbers(int how_many)
    {
        nrs = new int[how_many];
        doRandomNr(); // genera numeri casuali da mettere in nrs
    }

    private void doRandomNr()
    {
        Random rnd = new Random();

        for (int n = 0; n < nrs.length; n++)
            nrs[n] = rnd.nextInt(100);
    }

    public Iterator<Integer> iterator()
    {
        return new Iterator<>() // creo un oggetto che implementa l'interfaccia Iterator
        {
            int pos = 0;

            public boolean hasNext() { return pos >= nrs.length ? false : true; }
            public Integer next() { return nrs[pos++]; }
            public void remove() 
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
}

public class MyIterable
{
    public static void main(String args[])
    {
        Numbers nrs_obj = new Numbers(5); // creo un oggetto Numbers con 5 elementi

        for (Number n : nrs_obj) // ciclo i suoi elementi
            System.out.print(n + " ");
    }
}
