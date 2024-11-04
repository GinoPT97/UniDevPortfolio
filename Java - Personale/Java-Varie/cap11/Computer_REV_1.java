package com.pellegrinoprincipe.hard;

public class Computer_REV_1
{
    private String os;

    Printer p = new Printer(); // oggetto di tipo Printer

    public enum Hardware { MOUSE, KEYBOARD; }

    public void setOS(String os) { this.os = os;}  
    public String getOS() { return os; }
    public void setPrinterName(String name) { p.name = name; }
    public String getPrinterName() { return p.name; }
}
