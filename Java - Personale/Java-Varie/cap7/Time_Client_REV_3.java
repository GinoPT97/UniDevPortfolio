package com.pellegrinoprincipe;

public class Time_Client_REV_3
{
    public static void main(String[] args)
    {
        // invocazione dei vari costruttori di Time_REV_3
        Time_REV_3 t = new Time_REV_3(4);
        Time_REV_3 t2 = new Time_REV_3(18, 30);
        Time_REV_3 t3 = new Time_REV_3(t2);

        System.out.print("[t = " + t.getOra() + ":" + t.getMinuti() + ":" + t.getSecondi());
        System.out.print("] [t2 = " + t2.getOra() + ":" + t2.getMinuti() + ":" + t2.getSecondi());
        System.out.println("] [t3 = " + t3.getOra() + ":" + t3.getMinuti() + ":" + t3.getSecondi() + "]");
    }
}
