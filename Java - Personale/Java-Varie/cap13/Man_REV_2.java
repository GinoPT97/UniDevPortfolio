package com.pellegrinoprincipe;

/**
 * <p><b>Classe</b> per la gestione di un generico uomo</p>
 *
 * @author Pellegrino ~thp~ Principe
 * @see com.pellegrinoprincipe.Time_REV_7
 * @version 1.0
 */
public class Man_REV_2
{
    /**
     * Indica il cognome di un uomo
     */
    private String cognome;
    /**
     * Indica il nome di un uomo
     */
    private String nome;
    /**
     * Indica un oggetto <code>Time_REV_7</code>
     */
    private Time_REV_7 time_to_work;

    /**
     * Crea un oggetto di tipo Man_REV_2
     *
     * @param c  indica il cognome
     * @param n  indica l nome
     * @param o  indica le ore di inizio lavoro
     * @throws Exception se l'orario e' superiore alle 9
     */
    public Man_REV_2(String c, String n, int o) throws Exception
    {
        cognome = c;
        nome = n;

        if (o > 9)
            throw new Exception("Attenzione il lavoro inizia prima delle 9!");
        else
            time_to_work = new Time_REV_7(o);
    }

    /**
     * Ritorna una rappresentazione leggibile di un oggetto Man_REV_2
     *
     * @return una <code>String</code> che rappresenta un Man_REV_2
     */
    public String toString()
    {
        return cognome + " " + nome + " va a lavorare alle ore: " + time_to_work.getOra();
    }
}
