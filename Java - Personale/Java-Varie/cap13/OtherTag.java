package com.pellegrinoprincipe;

import java.io.IOException;

/**
 * <p><b>Classe</b> OtherDocumentation</p>
 *
 * @author Pellegrino ~thp~ Principe
 * @version 1.0
 */
class OtherDocumentation
{
    /**
     * un metodo generico...
     */
    public void Bar() {}
}

/**
 * <p><b>Classe</b> OtherTag</p>
 *
 * @author Pellegrino ~thp~ Principe
 * @version 1.1
 */
public class OtherTag extends OtherDocumentation
{
    /**
     * __do Valore costante: {@value}
     */
    private final int __do = 10;
    
    /**
     *  Copyright image for the following image {@docRoot}/img/copyright.png
     */
    private String trash_image = "trash.png";

    /**
     * @deprecated
     * metodo foo
     * @param g indica un intero...
     */
    public void foo(int g) {}

    /**
     * doStuff {@code metodo <doStuff>}
     * @since 1.0 Nuova versione {@link #doStuff(int a) }
     */
    public void doStuff(){}

    /**
     * doStuff {@code <doStuff> new version method}
     * @param a indica un intero...
     * @since 1.1
     */
    public void doStuff(int a) {}

    /**
     * metodo per le somme
     * @throws IOException
     */
    public void makeSum() throws IOException {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void Bar(){}
}
