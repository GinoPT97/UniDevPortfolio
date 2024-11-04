package com.pellegrinoprincipe;

class CalculateMax
{
    public CalculateMax() {}

    public double maximum(double a, double b, double c) // massimo tra valori double
    {
        double max = a;

        if (b > a)
            max = b;
        if (c > b)
            max = c;
 
         return max;
    }
    public int maximum(int a, int b, int c) // massimo tra valori interi
    {
        int max = a;

        if (b > a)
            max = b;
        if (c > b)
            max = c;

        return max;
    }
    public char maximum(char a, char b, char c) // massimo tra valori carattere
    {
        char max = a;

        if (b > a)
            max = b;  
        if (c > b)
            max = c;

        return max;
    }
}

public class CalculateMaxClient
{
    public static void main(String[] args)
    {
        CalculateMax cm = new CalculateMax();

        Double d[] =  { 11.1, 11.2, 9.6 };
        Integer i[] = { 12, 13, 3 };
        Character c[] = { 'n', 'b', 'z' };

        System.out.print("Max (double): " + cm.maximum(d[0], d[1], d[2]));
        System.out.print(" | Max (int): " + cm.maximum(i[0], i[1], i[2]));
        System.out.println(" | Max (char): " + cm.maximum(c[0], c[1], c[2]));
    }
}
