package com.pellegrinoprincipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class Algorithms
{
    public static void main(String args[])
    {
        String os[] = {"GNU/Linux","Windows 7","Solaris","Amiga OS","FreeBSD","Mac OS X"};

        LinkedList<String> al_operating_systems = new LinkedList<>(Arrays.asList(os)); // Lista di os        
        Collections.sort(al_operating_systems); // ordina la collezione
        System.out.print("Collezione ordinata: ");
        System.out.println(al_operating_systems);

        int ix = Collections.binarySearch(al_operating_systems, "Be OS", null); // ricerca se presente l'elemento Be Os
        if (ix < 0)// se non è presente aggiungilo
           al_operating_systems.add(-(ix + 1), "Be OS");
  
        System.out.print("Collezione con elemento Be OS aggiunto: ");
        System.out.println(al_operating_systems);

        Collections.rotate(al_operating_systems, 3); // ruota gli elementi di 3 posizioni
        System.out.print("Collezione con elementi ruotati: ");
        System.out.println(al_operating_systems);

        System.out.print("Elemento della collezione minore: ["); // elemento più piccolo
        System.out.println(Collections.min(al_operating_systems) + " ]");

        System.out.print("Elemento della collezione maggiore: [");// elemento più grande
        System.out.println(Collections.max(al_operating_systems) + " ]");
    }
}
