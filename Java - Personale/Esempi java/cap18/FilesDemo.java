package com.pellegrinoprincipe;

import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.lang.System.out;
import static java.nio.file.StandardCopyOption.*;

public class FilesDemo
{
    public static void main(String[] args) throws IOException
    {
        Path path = Paths.get("C:\\MY_JAVA_SOURCES\\Test.java");
        Path other_path = Paths.get("C:\\MY_JAVA_SOURCES\\InData.txt");
        Path link_path = Paths.get("C:\\MY_JAVA_SOURCES\\in"); // symbolic link
        Path copy_path = Paths.get("C:\\MY_JAVA_CLASSES\\InData.txt");

        // test esistenza file      
        String ex_1 = Files.exists(path) ? "esiste" : "non esiste";
        String ex_2 = Files.exists(other_path) ? "esiste" : "non esiste";
        out.format("Il file %s %s. \n", path, ex_1);
        out.format("Il file %s %s. \n", other_path, ex_2);

        // test se due path localizzano lo stesso file
        boolean path_eq = Files.isSameFile(other_path, link_path);
        out.format("Il file %s è lo stesso del file %s [%b] %n", other_path, link_path, path_eq);

        try // cancello un file che non esite!
        {
            Files.delete(Paths.get("C:\\MY_JAVA_SOURCES\\nofile.txt"));
        }
        catch (NoSuchFileException fse)
        {
            out.format("Cancellazione fallita: il file %s non esiste %n", fse.getFile());
        }

        try // copio il file InData.txt
        {
            Files.copy(other_path, copy_path, COPY_ATTRIBUTES, REPLACE_EXISTING);            
        }
        catch (FileAlreadyExistsException fae)
        {
            out.println("Copia fallita: forse la directory non è vuota?");
        }
        
        try // sposto il file InData.txt
        {
            Files.move(copy_path, other_path, ATOMIC_MOVE, REPLACE_EXISTING);
        }
        catch(AtomicMoveNotSupportedException am )
        {
            out.println("Il filesytem non supporta lo spostamento 'atomico'.");
        }
    }
}