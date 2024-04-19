package com.pellegrinoprincipe;

public class Man_Client_REV_1
{
    public static void main(String[] args)
    {
        // creo tre istanze di Man_REV_1
        Man_REV_1 a_man1 = new Man_REV_1("Principe", "Pellegrino", 8);
        Man_REV_1 a_man2 = new Man_REV_1("Rizzo", "Stefano", 9);
        Man_REV_1 a_man3 = new Man_REV_1("Rossi", "Alberto", 10);

        // stampo quanto istanze sono state create
        System.out.println("Sono stati creati " + Man_REV_1.how_many + " uomini...");
    }
}
