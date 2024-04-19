package com.pellegrinoprincipe;

public class LogicalOperators
{
    public static void main(String args[])
    {
        // matrice per la ricerca
        int[][] values = {{10, 100, 30}, {-22, -11, 66}, {105, 204, 333}};
        int filter_value = 33; // valori da confrontare
        int found_values[] = new int[9]; // numero massimo di elementi da inserire

        // ciclo per la ricerca
        for (int i = 0; i < values.length; i++)
        {
            for (int j = 0; j < values[i].length; j++)
            {
                int value = values[i][j];
                // posiziono il valore trovato nell'array spostandomi alla corretta posizione
                if (value % 2 == 0)
                    found_values[i * values[j].length + j] = value > filter_value ? value : 0;
            }
        }
        // valori trovati
        for (int i = 0; i < found_values.length; i++)
            System.out.print(found_values[i] + " ");
     
        System.out.println();
    }
}
