package com.pellegrinoprincipe;

public class Time_Client_Array
{
    public static void main(String[] args)
    {
        // array di Time_REV_5
        Time_REV_5 array_t1[] =
        {
            new Time_REV_5(10, 00, 00), new Time_REV_5(11, 00, 00), new Time_REV_5(12, 00, 00)
        };

        for (int i = 0; i < 3; i++)
        {
            System.out.print("{t1[" + i + "] " + array_t1[i].getOra() + ":" + array_t1[i].getMinuti() + ":"
                    + array_t1[i].getSecondi() + "} ");
        }
    }
}
