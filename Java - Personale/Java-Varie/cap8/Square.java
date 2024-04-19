package com.pellegrinoprincipe;

public class Square extends Rectangle
{
    public Square() { width = 1;} 
    public Square(Point2D upperleftCoords, int side)
    {
        super(upperleftCoords, side, side);
    }

    protected void finalize() {}   // invocato dal garbage collector...    
    public int getSide() { return width; }

    public String toString()
    {
        return "QUADRATO { " + upperleftCoords + " --> Lato: " + width + " }";
    }
}
