package com.pellegrinoprincipe;

import java.util.ArrayList;

public class AnnSuppressWarnings
{
    @SuppressWarnings("unchecked")
    public static void addIntoAList()
    {
        ArrayList l = new ArrayList();
        l.add("...");
    }

    public static void main(String[] args)
    {
        addIntoAList();
    }
}
