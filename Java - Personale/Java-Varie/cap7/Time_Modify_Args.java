package com.pellegrinoprincipe;

public class Time_Modify_Args
{
    public static void modify(Time_REV_5 t_p)
    {
        t_p.setTime(11, 00, 00);
    }

    public static void main(String args[])
    {
        Time_REV_5 t = new Time_REV_5(18, 30, 0);
        System.out.print("{t = " + t.getOra() + ":" + t.getMinuti() + ":" + t.getSecondi() + "} ");

        modify(t); // modifichiamo

        System.out.println("{t modificato = " + t.getOra() + ":" + t.getMinuti() + ":" + t.getSecondi() + "}");
    }
}
