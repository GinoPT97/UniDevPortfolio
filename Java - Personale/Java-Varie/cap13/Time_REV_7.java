package com.pellegrinoprincipe;

/**
 * <p><b>Classe</b> per la gestione di un Time_REV_7</p>
 * @author Pellegrino ~thp~ Principe
 * 
 * @version 1.0
 */
public class Time_REV_7
{
    /**
     * Indica l'ora
     */
    private int ora;
    /**
     * Indica i minuti
     */
    private int minuti;
    /**
     *  Indica i secondi
     */
    private int secondi;
    /**
     * Indica un messaggio generico
     */
    private final String MSG = "Ora assegnata = ";

    /**
     * Crea un oggetto di tipo Time_REV_7 con valori di
     * default
     */
    public Time_REV_7()
    {
        setTime(0, 0, 0);
    }

    /**
     * Crea un oggetto di tipo Time_REV_7 con
     * la possibilita' di passare l'ora
     *
     * @param h indica l'ora
     */
    public Time_REV_7(int h)
    {
        setTime(h, 0, 0);
    }

    /**
     * Crea un oggetto di tipo Time_REV_7 con
     * la possibilita' di passare l'ora e i
     * minuti
     *
     * @param h indica l'ora
     * @param m indica i minuti
     */
    public Time_REV_7(int h, int m)
    {
        setTime(h, m, 0);
    }

    /**
     * Crea un oggetto di tipo Time_REV_7 con
     * la possibilita' di passare l'ora, i minuti
     * e i secondi
     *
     * @param h indica l'ora
     * @param m indica i minuti
     * @param s indica i secondi
     */
    public Time_REV_7(int h, int m, int s)
    {
        setTime(h, m, s);
    }

    /**
     * Crea un oggetto di tipo Time_REV_7 con
     * la possibilita' di passare un oggetto
     * del suo stesso tipo
     *
     * @param t indica un oggetto Time_REV_7
     */
    public Time_REV_7(Time_REV_7 t)
    {
        setTime(t.ora, t.minuti, t.secondi);

    }

    /**
     * Imposta un orario
     *
     * @param o indica l'ora
     * @param m indica i minuti
     * @param s indica i secondi
     *
     * @see com.pellegrinoprincipe.Time_REV_7#setOra
     * @see Time_REV_7#setMinuti
     * @see #setSecondi
     */
    public void setTime(int o, int m, int s)
    {
        setOra(o);
        setMinuti(m);
        setSecondi(s);
    }

    /**
     * Imposta l'ora
     *
     * @param o indica l'ora
     */
    public void setOra(int o)
    {
        ora = (o < 24 && o >= 0) ? o : 0;
    }

    /**
     * Imposta i minuti
     *
     * @param m indica i minuti
     */
    public void setMinuti(int m)
    {
        minuti = (m < 60 && m >= 0) ? m : 0;
    }

    /**
     * Imposta i secondi
     *
     * @param s indica i secondi
     */
    public void setSecondi(int s)
    {
        secondi = (s < 60 && s >= 0) ? s : 0;
    }

    /**
     * Ritorna un orario
     *
     * @return un oggetto <code>String</code> con l'orario
     */
    public String getTime()
    {
        return ora + ":" + minuti + ":" + secondi;
    }

    /**
     * Ritorna l'ora
     *
     * @return un <code>int</code> con l'orario
     */
    public int getOra()
    {
        return ora;
    }

    /**
     * Ritorna i minuti
     *
     * @return  un <code>int</code> con i minuti
     */
    public int getMinuti()
    {
        return minuti;
    }

    /**
     * Ritorna i secondi
     *
     * @return un <code>int</code> con i secondi
     */
    public int getSecondi()
    {
        return secondi;
    }

    /**
     * Ritorna una rappresentazione leggibile di un oggetto Time_REV_7
     *
     * @return una <code>String</code> che rappresenta un Time_REV_7
     *
     */
    public String toString()
    {
        return MSG + getTime();
    }
}
