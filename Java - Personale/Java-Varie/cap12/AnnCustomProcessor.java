package com.pellegrinoprincipe;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes( { "com.pellegrinoprincipe.WorkToDo_REV_1" } )
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnCustomProcessor extends AbstractProcessor
{
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        for (Element elems : roundEnv.getElementsAnnotatedWith(WorkToDo_REV_1.class))
        {
            System.out.println("METODO ANNOTATO: " + elems.toString()); // metodo dell'annotazione
            WorkToDo_REV_1 wtd = elems.getAnnotation(WorkToDo_REV_1.class);

            // output dati dell'annotazione
            System.out.println("Sviluppatore: " + wtd.developer());
            System.out.println("Messaggio: " + wtd.msg());
            System.out.println("Data inizio: " + wtd.start_date());
            System.out.println("ID: " + wtd.uid());
        }
        return true;
    }
}
