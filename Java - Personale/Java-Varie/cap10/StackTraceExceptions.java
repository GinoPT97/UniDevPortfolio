package com.pellegrinoprincipe;

public class StackTraceExceptions
{
    public static void exc() throws Exception // metodo che lancia un'eccezione
    {
        throw new Exception("Ops...");
    }

    public static void main(String args[])
    {
        try
        {
            exc();
        }
        catch (Exception e)
        {
            System.out.println("Messaggio dell'eccezione: " + e.getMessage());

            // stampa lo stack trace attraverso l'utilizzo di oggetti di tipo StackTraceElement
            System.out.println("Stack trace dell'eccezione: ");
            StackTraceElement st[] = e.getStackTrace();
            for (StackTraceElement el : st)
                System.out.println("CLASSE: " + el.getClassName() + "\n"
                                              + "METODO: " + el.getMethodName() + "\n" + "FILE: "
                                              + el.getFileName() + "\n" + "NUMERO DI LINEA: "
                                              + el.getLineNumber());
       }
    }
}
