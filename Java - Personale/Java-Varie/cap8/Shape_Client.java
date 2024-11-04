package com.pellegrinoprincipe;

public class Shape_Client
{
    public static void main(String args[])
    {
        TwoDShape tds;
        Rectangle_REV_1 r = new Rectangle_REV_1(new Point2D(10, 10), 5, 4);
        Square_REV_1 s = new Square_REV_1(new Point2D(35, 40), 9);

        tds = r; // TwoDShape ora è un tipo Rectangle
        tds.draw();

        tds = s; // TwoDShape ora è un tipo Square
        tds.draw();
    }
}

