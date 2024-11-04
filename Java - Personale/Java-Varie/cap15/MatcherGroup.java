package com.pellegrinoprincipe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherGroup
{
    public static void main(String[] args)
    {
        String to_compare = "Pellegrino, Rino, Gennarino";
        String regex = "(?i)R(ino)";
        Matcher m = Pattern.compile(regex).matcher(to_compare);

        while (m.find()) // scorre tutte le occorrenze trovate
        {
            System.out.println("CORRISPONDENZA TROVATA:");
            System.out.println("Gruppo 0: " + m.group(0) + " alla posizione: " + m.start(0));
            System.out.println("Gruppo 1: " + m.group(1) + " alla posizione: " + m.start(1));
            System.out.println("---------------------------------------------");
        }
    }
}
