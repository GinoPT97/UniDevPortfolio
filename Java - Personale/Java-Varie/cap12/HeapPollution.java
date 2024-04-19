package com.pellegrinoprincipe;

import java.util.ArrayList;
import java.util.List;

public class HeapPollution
{
    public static <T> void add(List<T> l, T... elems)
    {
        for (T e : elems)
            l.add(e);
    }

    public static void main(String[] args)
    {
        List<String> l = new ArrayList<>();
        add(l, "Mac OS", "Windows", "Linux");        
    }
}
