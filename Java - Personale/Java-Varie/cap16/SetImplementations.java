package com.pellegrinoprincipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class SetImplementations
{
    // stampa tutti gli elementi di una collezione
    public static void printAllCollectionElements(Collection<String> c, String from)
    {
        System.out.println("Tutte le keywords di Java dalla collezione di tipo " + from);

        int sep = 1;
        System.out.println("[");
        for (String k : c)
        {
            System.out.print(k + " ");
            if (sep % 10 == 0)
                System.out.println();
            sep++;
        }
        System.out.println("]");
    }

    public static void main(String args[])
    {
        String java_keywords[] =
        {
            "abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package",
            "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements",
            "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof",
            "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface",
            "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native",
            "super", "while"
        };

        Set<String> hs_keywords = new HashSet<>(25, 0.6f); // HashSet
        for (String s : java_keywords) // aggiungo gli elementi all'insieme
            hs_keywords.add(s);

        Set<String> ts_keywords = new TreeSet<>(hs_keywords); // TreeSet

        Collection<String> a_c = Arrays.asList(java_keywords); // LinkedHashSet
        Set<String> ls_keywords = new LinkedHashSet<>(a_c);

        // manda in stampa gli elementi delle collezioni in successione
        printAllCollectionElements(hs_keywords, "HashSet");
        printAllCollectionElements(ts_keywords, "TreeSet");
        printAllCollectionElements(ls_keywords, "LinkedHashSet");
    }
}
