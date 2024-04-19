package com.pellegrinoprincipe;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class MapImplementations
{
    public static void main(String args[])
    {
        Map<String, String> m_city_regions = new HashMap<>(); // HashMap

        // aggiungo le città
        m_city_regions.put(new String("Napoli"), "Campania");
        m_city_regions.put(new String("Salerno"), "Campania");
        m_city_regions.put(new String("Caserta"), "Campania");
        m_city_regions.put(new String("Avellino"), "Campania");
        m_city_regions.put(new String("Benevento"), "Lombardia"); // ATTENZIONE errore regione... 
        m_city_regions.put(new String("Benevento"), "Campania"); // ... lo sostituisco con questo...

        System.out.println("Città della Campania contenute in un HashMap:\n" + m_city_regions);

        Map<String, String> im_city_regions = new IdentityHashMap<>(); // IdentityHashMap

        // aggiungo le città
        im_city_regions.put(new String("Napoli"), "Campania");
        im_city_regions.put(new String("Salerno"), "Campania");
        im_city_regions.put(new String("Caserta"), "Campania");
        im_city_regions.put(new String("Avellino"), "Campania");        
        im_city_regions.put(new String("Benevento"), "Lombardia"); // ATTENZIONE errore regione...
        
        // ... lo sostituisco con questo... ma non funziona e l'entry è duplicata!!!
        im_city_regions.put(new String("Benevento"), "Campania");

        System.out.println("Città della Campania contenute in un IdentityHashMap:\n" + im_city_regions);
    }
}
