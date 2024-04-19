package com.pellegrinoprincipe;

class PrintArray
{
    public PrintArray() {}

    public void printArray(Integer el[]) // stampa un array di interi
    {
        for (Integer i : el)
            System.out.print(i + " ");
    }
    public void printArray(Double el[]) // stampa un array di double
    {
        for (Double i : el)
            System.out.print(i + " ");
    }
    public void printArray(Character el[]) // stampa un array di caratteri
    {
        for (Character i : el)
            System.out.print(i + " ");
    }
} 

public class PrintArrayClient
{
    public static void main(String[] args)
    {
        PrintArray pa = new PrintArray();

        Double d[] = { 11.1, 11.2 };
        Integer i[] = { 12, 13 };
        Character c[] = { 'a', 'b'};

        System.out.print("[ ");
        pa.printArray(d); 
        pa.printArray(i);
        pa.printArray(c);
        System.out.print("]");
    }
}
