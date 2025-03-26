package com.pellegrinoprincipe;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.logging.Logger;

@SupportedAnnotationTypes( { "com.pellegrinoprincipe.WorkToDo_REV_1" } )
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnCustomProcessor extends AbstractProcessor {
    private static final Logger LOGGER = Logger.getLogger(AnnCustomProcessor.class.getName());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element elems : roundEnv.getElementsAnnotatedWith(WorkToDo_REV_1.class)) {
            LOGGER.info("METODO ANNOTATO: " + elems.toString()); // metodo dell'annotazione
            WorkToDo_REV_1 wtd = elems.getAnnotation(WorkToDo_REV_1.class);

            // output dati dell'annotazione
            LOGGER.info("Sviluppatore: " + wtd.developer());
            LOGGER.info("Messaggio: " + wtd.msg());
            LOGGER.info("Data inizio: " + wtd.start_date());
            LOGGER.info("ID: " + wtd.uid());
        }
        return true;
    }
}
