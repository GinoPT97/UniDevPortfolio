package com.pellegrinoprincipe;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Buffer
{
    private int data[]; // dato condiviso
    private int nr_elem = 10; // max elem. di default
    private boolean empty = true; // stato buffer all'inizio
    private Lock object_lock; // lock
    private Condition write_codition;  // condizioni per il lock
    private Condition read_condition;

    // inizializzo il buffer
    public Buffer(int elem)
    {
        if (elem != -1)
        {
            data = new int[elem];
            nr_elem = elem;
        }
        else
            data = new int[nr_elem];
 
        object_lock = new ReentrantLock();  // creo un oggetto per il lock
 
        // creo gli oggetti condizione
        write_codition = object_lock.newCondition();
        read_condition = object_lock.newCondition();
    }

    // scrivo nel buffer
    public void write(int val) throws InterruptedException
    {
        object_lock.lock(); // acquisisco il lock sull'oggetto
        try
        {
            while (!empty) // finché il consumer non ha letto il dato aspetto
             {
                System.out.println(Thread.currentThread().getName() + " attende per CONSUMER legga il dato...");
                write_codition.await();// aspetto
            }

            data[val] = val; // scrivo il dato
            empty = false; // aggiorno lo stato

            System.out.println(Thread.currentThread().getName()
                    + " ha scritto all'indice " + val + " il valore: " + data[val]);

            read_condition.signal(); // notifico il reader che può leggere
        }
        finally { object_lock.unlock(); /* rilascia il lock */ }
    }

    // leggo dal buffer
    public int read(int ix) throws InterruptedException
    {        
        object_lock.lock();// acquisisco il lock sull'oggetto

        try
        {
            while (empty)  // finché il producer non ha scritto il dato aspetto
            {
                System.out.println(Thread.currentThread().getName() + " attende che PRODUCER scriva il dato...");
                read_condition.await(); // aspetto
            }
            
            empty = true; // aggiorno lo stato
            System.out.println(Thread.currentThread().getName()
                    + " ha letto all'indice " + ix + " il valore: " + data[ix]);
            
            write_codition.signal(); // notifico il writer che può scrivere
        }
        finally { /* rilascia il lock */ object_lock.unlock(); }

        return data[ix];
    }

    public int getBufferElements() { return nr_elem; }
}

class RunProducer implements Runnable // Runnable per il thread 1
{
    private Buffer b;

    public RunProducer(Buffer b) { this.b = b; }

    public void run()
    {
        for (int i = 0; i < b.getBufferElements(); i++)
        {
            Random r = new Random(); // crea un intervallo di tempo random tra 0 e 5 secondi
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

    public RunConsumer(Buffer b){ this.b = b; }

    public void run()
    {
        for (int i = 0; i < b.getBufferElements(); i++)
        {
            Random r = new Random(); // crea un intervallo di tempo random tra 0 e 5 secondi
            int ms = r.nextInt(5000);
            try
            {
                Thread.sleep(ms); // un pò di attesa...
                b.read(i);
            }
            catch (InterruptedException ex){}
        }
    }
}

public class CoopSynchronizedThread_REV_1
{
    public static void main(String args[])
    {
        Buffer b = new Buffer(-1); // Buffer
        Thread t_1 = new Thread(new RunProducer(b), "PRODUCER");
        Thread t_2 = new Thread(new RunConsumer(b), "CONSUMER");

        // avvio i threads
        t_1.start();
        t_2.start();
    }
}
