package com.pellegrinoprincipe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import static java.lang.System.out;

public class AttributeDemo
{
    public static void main(String[] args) throws IOException
    {
        Path path = Paths.get("C:\\MY_JAVA_SOURCES\\InData.txt");

        // lettura attributi file
        out.format("Attributi del file %s%n", path.getFileName() + ":");
        
        BasicFileAttributes basic_attrs = Files.readAttributes(path, BasicFileAttributes.class);
        
        out.format("Data creazione: %s%n", basic_attrs.creationTime());
        out.format("Data ultimo accesso: %s%n", basic_attrs.lastAccessTime());
        out.format("Data ultima modifica: %s%n", basic_attrs.lastModifiedTime());
        out.format("E' una directory? %s%n", basic_attrs.isDirectory());
        out.format("E' un file? %s%n", basic_attrs.isRegularFile());
        out.format("E' un link simbolico? %s%n", basic_attrs.isSymbolicLink());
        out.format("Dimensione in byte: %d%n", basic_attrs.size());
    }
}
