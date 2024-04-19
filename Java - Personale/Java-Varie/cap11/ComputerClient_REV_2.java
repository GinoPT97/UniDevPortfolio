package com.thp;

import com.pellegrinoprincipe.hard.*;
import com.pellegrinoprincipe.soft.*;

public class ComputerClient_REV_2
{
    public static void main(String[] args)
    {
        Computer_REV_1 c = new Computer_REV_1();

        c.setOS("GNU/LINUX");
        System.out.println("OS = " + c.getOS());

        Software s = new Software();
        Software.Graphic g = Software.Graphic.PHOTOSHOP;
        s.setGraphic(g);
        System.out.println("SOFTWARE = " + s.getGraphic());

        // impostiamo ed otteniano il nome della stampante in uso
        // tramtie metodi get e set della classe Computer_REV_1
        c.setPrinterName("EPSON");
        System.out.println(c.getPrinterName());

        // oggetto di tipo Printer
        Printer p = new Printer();

        // attenzione accesso diretto non consentito perché Printer appartiene ad un package differente
        p.name = "HP"; // ERRORE
    }
}
