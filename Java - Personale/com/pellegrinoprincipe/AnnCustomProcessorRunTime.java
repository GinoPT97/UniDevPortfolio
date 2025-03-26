package com.pellegrinoprincipe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class AnnCustomProcessorRunTime {
    private static final Logger LOGGER = Logger.getLogger(AnnCustomProcessorRunTime.class.getName());

    public static void main(String[] args) throws NoSuchMethodException {
        Class<AnnCustom_REV_2> classObj = AnnCustom_REV_2.class; // classe dove è presente l'annotazione
        Method[] methods = classObj.getMethods(); // metodi della classe

        for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations(); // ottengo l'annotazione del metodo

            if (methodAnnotations.length > 0) {
                LOGGER.info("METODO ANNOTATO: " + method.getName()); // metodo dell'annotazione
                for (Annotation annotation : methodAnnotations) {
                    if (annotation instanceof WorkToDo_REV_2) { // stampo i valori dell'annotazione
                        WorkToDo_REV_2 wtd = (WorkToDo_REV_2) annotation;

                        LOGGER.info("Sviluppatore: " + wtd.developer());
                        LOGGER.info("Messaggio: " + wtd.msg());
                        LOGGER.info("Data inizio: " + wtd.start_date());
                        LOGGER.info("ID: " + wtd.uid());
                    }
                }
            }
        }
    }
}
