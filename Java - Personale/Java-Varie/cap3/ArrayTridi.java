package com.pellegrinoprincipe;

public class ArrayTridi
{
    public static void main(String[] args)
    {
        // array 3D - x,y,z
        int[][][] points = {{{100, 120, 20},{100, 150, 80},{20, 330, 400}}};

        for (int x = 0; x < points.length; x++)
        {
            for (int y = 0; y < points[x].length; y++)
            {
                System.out.print("Stampo punto alle coordinate (X, Y, Z) --> ");                
                for (int z = 0; z < points[x][y].length; z++)
                     System.out.print(points[x][y][z] + " ");
                System.out.println();
            }
        }
    }
}
