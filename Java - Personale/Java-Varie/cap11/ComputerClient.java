package com.thp;

import com.pellegrinoprincipe.hard.*;

public class ComputerClient
{
    public static void main(String[] args)
    {
        Computer c = new Computer(); // istanza di un oggetto Computer
        c.setOS("GNU/LINUX");
        System.out.println("OS = " + c.getOS());
    }
}
