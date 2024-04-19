package com.pellegrinoprincipe;

public class SwitchCaseAnnidati
{
    public static void main(String[] args)
    {
        int exp = 2, exp2 = 1;

        switch (exp) // confronta exp  
        {
            case 1:
                System.out.println("exp = " + exp);
                break;
            case 2: // confronta exp2 nello SWITCH INDENTATO                
                switch (exp2)
                {
                    case 1:
                        System.out.println("exp = " + exp + " and exp2 = " + exp2);
                        break;
                }
                break;
        }
    }
}
