package com.pellegrinoprincipe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnCustomProcessorRunTime
{
    public static void main(String args[]) throws NoSuchMethodException
    {
        Class<AnnCustom_REV_2> class_obj = AnnCustom_REV_2.class; // classe dove è presente l'annotazione
        Method[] ms = class_obj.getMethods(); // metodi della classe

        for (Method m : ms)
        {
            Annotation[] method_annotations = m.getAnnotations(); // ottengo l'annotazione del metodo

            if (method_annotations.length > 0)
            {
                System.out.println("METODO ANNOTATO: " + m.getName()); // metodo dell'annotazione
                for (Annotation ann : method_annotations)
                {
                    if (ann instanceof WorkToDo_REV_2)// stampo i valori dell'annotazione
                    {
                        WorkToDo_REV_2 wtd = (WorkToDo_REV_2) ann;

                        System.out.println("Sviluppatore: " + wtd.developer());
                        System.out.println("Messaggio: " + wtd.msg());
                        System.out.println("Data inizio: " + wtd.start_date());
                        System.out.println("ID: " + wtd.uid());
                    }
                }
            }
        }
    }
}
