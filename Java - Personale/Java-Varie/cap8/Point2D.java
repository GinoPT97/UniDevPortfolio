package com.pellegrinoprincipe;

public class Point2D
{
    private int x;
    private int y;

    // costruttori
    public Point2D() { x = y = 0; }
    public Point2D(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    protected void finalize() {}   // invocato dal garbage collector...
    protected int getX() { return x; }
    protected int getY() { return y; }

    // overriding di toString da Object
    public String toString()
    {
        return "[ " + x + " , " + y + " ]";
    }
}
