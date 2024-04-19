package com.pellegrinoprincipe;

// classe Corridor
class Corridor
{
    private boolean free;

    public void enterIntoHall() // entrata nell'ingresso
    {
        System.out.println(Thread.currentThread().getName() + " entra nella hall...");
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex) {}
        
        checkCorridor(); // il corridoio è libero?  
    }

    public synchronized void checkCorridor()
    {
        System.out.println(Thread.currentThread().getName() + " verifica se il corridoio è libero...");
        try
        {
            wait(1000);
        }
        catch (InterruptedException ex) {}
        
        if (!free) // se il corridoio non è libero spostati
        {
            System.out.println(Thread.currentThread().getName() + " rileva che il corridoio non è libero...");
            try
            {
                wait(1000);
            }
            catch (InterruptedException ex) {}

            moveToSide();
        }

        walkIntoCorridor(); // cammina nel corridoio solo se è libero!!!
    }

    public synchronized void moveToSide() // movimento di lato
    {
        System.out.println(Thread.currentThread().getName() + " si muove di lato per renderlo disponibile...");
        try
        {
            wait(1000);
        }
        catch (InterruptedException ex) {}

        returnToCorridor();  // verifica nuovamente se il corridoio è libero
    }

    public synchronized void returnToCorridor()
    {
        System.out.println(Thread.currentThread().getName() + " ritorna verso il corridoio...");
        try
        {
            wait(1000);
        }
        catch (InterruptedException ex) {}

        checkCorridor();
    }

    public synchronized void walkIntoCorridor() // cammino nel corridoio
    {
        System.out.println(Thread.currentThread().getName()
                + " rileva che il corridoio è libero e inizia a cammminare nel corridoio...");
    }
}

public class Livelock
{
    public static void main(String args[])
    {
        final Corridor cor = new Corridor();

        Thread one = new Thread(new Runnable() // creo il primo thread
        {
            public void run() { cor.enterIntoHall(); }
        }, "Dario");
        one.start();

        Thread two = new Thread(new Runnable() // creo il secondo thread
        {
            public void run() { cor.enterIntoHall(); }
        }, "Francesco");
        two.start();
    }
}
