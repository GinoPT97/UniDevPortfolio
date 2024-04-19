package com.pellegrinoprincipe;

enum OS
{
    WINDOWS("XP"),
    LINUX("RedHat"),
    MAC("Jaguar"); // qui punto e virgola IMPORTANTE!!!
    
    private final String title;

    OS(String t) { title = t; }

    public String getTitle() { return title; }
}

public class EnumInAction
{
    public static void main(String[] args)
    {
        System.out.print("Tipi di OS: ");

        for (OS tmp : OS.values()) // cicliamo nell'array di OS
            System.out.print("[ " + tmp + " titolo " + tmp.getTitle() + " ]");
 
        System.out.print("\nOS scelto: ");

        OS a_s = OS.MAC; // assegniamo un valore di OS
        switch (a_s) // stampiamo quello scelto
        {
            case WINDOWS:
                System.out.println("Windows... ");
                break;
            case LINUX:
                System.out.println("Linux... ");
                break;
            case MAC:
                System.out.println("Mac... ");
                break;
            default:
                break;
        }
    }
}
