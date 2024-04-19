package com.pellegrinoprincipe;

import java.util.Random;

class Buffer
{
    private int[] data = new int[20];

    public synchronized void initData() // inizializza i dati
    {
        System.out.println(Thread.currentThread().getName() + " sta inizializzando l'array...");
        for (int i = 0; i < data.length; i++)
            data[i] = new Random().nextInt(500); // genera un valore random
        readData();
    }
    public synchronized void readData() // legge i dati
    {
        System.out.println(Thread.currentThread().getName() + " sta leggendo l'array...");
        for (int i = 0; i < data.length; i++)
        {
            int j = data[i] * new Random().nextInt(20); // scrive il dato rielaborandolo
            while (!writeData(i, j));
        }
    }
    public synchronized boolean writeData(int ix, int d) // scrive i dati
    {
        System.out.println(Thread.currentThread().getName() + " sta scrivendo nell'array...");
        data[ix] = d;
        return false; // causa un ciclo infinito
    }
}

public class Starvation
{
    public static void main(String args[])
    {
        final Buffer ba = new Buffer();

        Thread one = new Thread(new Runnable() // creo il primo thread
        {
            public void run() { ba.initData(); }
        });
        one.setPriority(Thread.MAX_PRIORITY);
        one.start();

        Thread two = new Thread(new Runnable() // creo il secondo thread
        {
            public void run() { ba.initData(); }
        });
        two.setPriority(Thread.MIN_PRIORITY);
        two.start();
    }
}
