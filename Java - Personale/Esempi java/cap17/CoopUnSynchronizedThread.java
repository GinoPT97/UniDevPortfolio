package com.pellegrinoprincipe;

import java.util.Random;

class Buffer
{
    private int data[]; // dato condiviso
    private int nr_elem = 10; // max elem. di default

    public Buffer(int elem) // inizializzo il buffer
    {
        if (elem != -1)
        {
            data = new int[elem];
            nr_elem = elem;
        }
        else
            data = new int[nr_elem];
    }

    public void write(int val)
    {
        data[val] = val;

        System.out.println(Thread.currentThread().getName()
                + " ha scritto all'indice " + val + " il valore: " + data[val]);
    }

    public int read(int ix)
    {
        System.out.println(Thread.currentThread().getName()
                + " ha letto all'indice " + ix + " il valore: " + data[ix]);

        return data[ix];
    }

    public int getBufferElements(){ return nr_elem; }
}

class RunProducer implements Runnable // Runnable per il thread 1
{
    private Buffer b;

    public RunProducer(Buffer b) { this.b = b; }

    public void run()
    {
        for (int i = 0; i < b.getBufferElements(); i++)
        {
            Random r = new Random();
            int ms = r.nextInt(5000);
            try
            {
                Thread.sleep(ms); // un pò di attesa...
                b.write(i);
            }
            catch (InterruptedException ex) {}
        }
    }
}

class RunConsumer implements Runnable // Runnable per il thread 2
{
    private Buffer b;

    public RunConsumer(Buffer b) { this.b = b; }

    public void run()
    {
        for (int i = 0; i < b.getBufferElements(); i++)
        {
            Random r = new Random();
            int ms = r.nextInt(1500);
            try
            {
                Thread.sleep(ms); // un pò di attesa...
                b.read(i);
            }
            catch (InterruptedException ex) {}
        }
    }
}

public class CoopUnSynchronizedThread
{
    public static void main(String args[])
    {
        Buffer b = new Buffer(-1); // buffer

        Thread t_1 = new Thread(new RunProducer(b), "PRODUCER"); // creo il PRODUCER
        Thread t_2 = new Thread(new RunConsumer(b), "CONSUMER"); // creo il CONSUMER

        // avvio i threads
        t_1.start();
        t_2.start();
    }
}
