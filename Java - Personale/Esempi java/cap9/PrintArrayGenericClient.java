package com.pellegrinoprincipe;

class PrintArrayGeneric
{
    public PrintArrayGeneric() {}

    public <E> void printArray(E el[])
    {
        for (E i : el) // stampa in modo generico gli elementi dell'array di differente tipo
            System.out.print(i + " ");
    }
}

public class PrintArrayGenericClient
{
    public static void main(String[] args)
    {
        PrintArrayGeneric pag = new PrintArrayGeneric();

        Double d[] =  { 11.1, 11.2 };
        Integer i[] = { 12, 13 };
        Character c[] = { 'a', 'b' };
        String s[] =  { "sono", "una", "stringa" };

        System.out.print("[ ");
        pag.printArray(d);
        pag.printArray(i);
        pag.printArray(c);
        pag.<String>printArray(s); // sintassi alternativa di invocazione di un metodo generico
        System.out.print("]");
    }
}