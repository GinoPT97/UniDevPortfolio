package com.pellegrinoprincipe;

public class ArrayBidi
{
    public static void main(String[] args)
    {
        // matrice di stringhe
        String[][] computer_table = 
        {
            {"MONITOR", "CASSE", "STAMPANTE", "PLOTTER"},
            {"MOUSE", "TASTIERA", "JOYPAD", "SCANNER"}
        };

        System.out.println("PERIFERICHE DI INPUT/OUTPUT");
        System.out.println("----------------------------------");

        // ciclo esterno
        for (int x = 0; x < computer_table.length; x++)
        {
            for (int y = 0; y < computer_table[x].length; y++)
                System.out.print(computer_table[x][y] + "  ");
            System.out.println();
        }
    }
}
