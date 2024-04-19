package com.pellegrinoprincipe;

public class SwitchCase
{
    public static void main(String[] args)
    {
        int number = 4;

        // valuto number
        switch (number)
        {
            case 1: // vale 1?
                System.out.println("Number: " + 1);
                break;
            case 2: // vale 2?
                System.out.println("Number: " + 2);
                break;
            case 3: // vale 3?
                System.out.println("Number: " + 3);
                break;
            case 4: // vale 4?
                System.out.println("Number: " + 4);
                break;
            default: // nessuna corrispondenza?
                System.out.println("Number: " + "none");
        }
    }
}