package com.pellegrinoprincipe;

class CalculateMaxGeneric
{
    public <T extends Comparable<T>> T maximum(T a, T b, T c)
    {
        T max = a;

        if (b.compareTo(a) > 0)
            max = b;
        if (c.compareTo(b) > 0)
            max = c;

        return max;
    }
}
public class CalculateMaxGenericClient
{
    public static void main(String[] args)
    {
        CalculateMaxGeneric cmg = new CalculateMaxGeneric();

        Double d[] = { 11.1, 11.2, 9.6 };
        Integer i[] = { 12, 13, 3 };
        Character c[] = { 'n', 'b', 'z' };
        String s[] = { "sono", "una", "stringa" };

        Double d_max = cmg.maximum(d[0], d[1], d[2]);
        Integer i_max = cmg.maximum(i[0], i[1], i[2]);
        Character c_max = cmg.maximum(c[0], c[1], c[2]);
        String s_max = cmg.maximum(s[0], s[1], s[2]);

        // stampa del valore massimo trovato
        System.out.print("Max (double): " + d_max);
        System.out.print(" | Max (int): " + i_max);
        System.out.print(" | Max (char): " + c_max);
        System.out.println(" | Max (String): " + s_max);
    }
}
