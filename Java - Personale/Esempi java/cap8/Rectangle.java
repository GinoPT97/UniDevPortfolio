package com.pellegrinoprincipe;

public class Rectangle
{
    protected int width;
    protected int height;
    protected Point2D upperleftCoords;

    // costruttori
    public Rectangle() 
    {
        width = height = 1; // rettangolo width * altezza di un'unità
        upperleftCoords = new Point2D(0, 0); // posizione di default
    }
    public Rectangle(Point2D upperleftCoords, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.upperleftCoords = upperleftCoords;
    }
    
    protected void finalize() {}   // invocato dal garbage collector...
    public int getWidth() { return width; }
    public int getHeight() { return height;}
    public Point2D getCoords() { return upperleftCoords; }    
    public int area() { return width * height; }    
    public int perimeter() { return 2 * width + 2 * height; }
        
    public String toString()
    {
        return "RETTANGOLO { " + upperleftCoords + " --> Larghezza: " + width + ", " + "Altezza: " + height + " } ";
    }
}
