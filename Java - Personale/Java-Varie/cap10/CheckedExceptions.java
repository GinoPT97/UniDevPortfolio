package com.pellegrinoprincipe;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CheckedExceptions
{
    public static Scanner FileScanner(String file_name) throws FileNotFoundException
    {
        return new Scanner(new File(file_name));
    }

    public static void main(String[] args)
    {
        try(Scanner n_scanner = FileScanner("Test.html")) // try-with-resource
        {
            System.out.println("Se vi è un'eccezione non saro' visualizzata!");
        }
        catch (FileNotFoundException fnf)
        {            
            System.out.println("ERRORE FILE NON TROVATO: " + "forse il nome file e' errato?");
        }
    }
}
