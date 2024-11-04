package com.thp;

import com.pellegrinoprincipe.hard.*;
import com.pellegrinoprincipe.soft.*;

public class ComputerClient_REV_1
{
    public static void main(String[] args)
    {
        Computer c = new Computer(); // oggetto di tipo Computer

        c.setOS("GNU/LINUX"); // imposto il sistema in uso
        System.out.println("OS = " + c.getOS());

        Software s = new Software(); // oggetto di tipo Software
        Software.Graphic g = Software.Graphic.PHOTOSHOP;
        s.setGraphic(g);
        System.out.println("SOFTWARE = " + s.getGraphic());
    }
}
