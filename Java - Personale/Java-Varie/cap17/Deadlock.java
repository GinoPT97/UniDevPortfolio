package com.pellegrinoprincipe;

class Door
{
    private Door other_door;
    private String porta;

    public Door(String p) { porta = p; }
    
    public synchronized void openDoor() // apre una porta
    {
        try
        {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " ha aperto la " + porta);
            System.out.println(Thread.currentThread().getName() + " sta tentando di aprire l'altra porta...");
            other_door.openOtherDoor();
        }
        catch (InterruptedException ex){}
    }
    public synchronized void openOtherDoor() // apre l'altra porta
    {
        try
        {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " ha aperto l'altra porta...");
        }
        catch (InterruptedException ex){}
    }

    public void setOtherDoor(Door other_door) { this.other_door = other_door; }
}

public class Deadlock
{
    public static void main(String args[])
    {
        final Door door_1 = new Door("porta 1");
        final Door door_2 = new Door("porta 2");

        door_1.setOtherDoor(door_2);
        door_2.setOtherDoor(door_1);

        new Thread(new Runnable() // creo il primo thread
        {
            public void run() { door_1.openDoor(); }
        }).start();

        new Thread(new Runnable() // creo il secondo thread
        {
            public void run() { door_2.openDoor(); }
        }).start();
    }
}
