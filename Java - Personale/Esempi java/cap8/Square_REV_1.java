package com.pellegrinoprincipe;

public class Square_REV_1 extends Rectangle_REV_1
{
    public Square_REV_1() { width = 1; }
    public Square_REV_1(Point2D upperleftCoords, int side)
    {
        super(upperleftCoords, side, side);
    }

    protected void finalize() {}   // invocato dal garbage collector...    
    public int getSide() {return width;}
    public void draw() { System.out.println("DISEGNO DEL QUADRATO"); };

    public String toString()
    {
        return "QUADRATO { " + upperleftCoords + " Lato: " + width + " }";
    }
}
