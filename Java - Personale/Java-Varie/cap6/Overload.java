package com.pellegrinoprincipe;

import java.io.*;

public class Overload
{
    private static int a = 10, b = 20;
    public static void result()
    {
        System.out.println("La somma di 10 e 20 e' " + (a + b));
    }

    public static void result(String file_name) throws IOException
    {
        if (file_name != null && file_name.length() > 0)
        {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file_name)))
            {
                out.write("La somma di 10 e 20 e' " + (a + b));
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        result("result.txt"); // scrivi su file
    }
}
