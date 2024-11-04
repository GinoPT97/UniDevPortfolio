package com.pellegrinoprincipe;

public class LabelError
{
    public static void main(String[] args)
    {
        // FORMA CORRETTA!!!
        label1:
        label2:
        {
            System.out.println("Sono nella label2");
            break label1;
        }

        // FORMA NON CORRETTA!!!
        label_a:
        {
            System.out.println("Sono nella label_a");
        }
        label_b:
        {
            System.out.println("Sono nella label_b ");
            break label_a;
        }
    }
}
