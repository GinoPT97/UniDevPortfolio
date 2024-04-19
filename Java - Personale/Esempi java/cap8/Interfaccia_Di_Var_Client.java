package com.pellegrinoprincipe;

interface OnlyVar
{
    int NO = 0;
    int YES = 1;
}

public class Interfaccia_Di_Var_Client implements OnlyVar
{
    public static void main(String args[])
    {
        System.out.println("NO = " + NO + " YES = " + YES);
    }
}
