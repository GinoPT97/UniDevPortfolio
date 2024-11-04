package com.pellegrinoprincipe;

public class StackGenericAndRawClient
{
    public static void main(String[] args)
    {
        Double d[] =  { 11.1, 11.2, 8.6 };

        StackGeneric sd = new StackGeneric(3); // classe row con tipo row
 
        for (double e : d) // test push
             sd.push(e);
  
        System.out.print("Valori dello stack Double: "); // test pop
        for (int nr = 0; nr < 3; nr++)
        {
            Double d_tmp = sd.pop(); // ERRORE di compilazione !!!
            System.out.print(d_tmp + " ");
        }
    }
}
