package com.pellegrinoprincipe;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.lang.System.out;

public class PathDemo
{
    public static void main(String[] args) throws IOException
    {
        Path path = Paths.get("C:\\MY_JAVA_SOURCES\\Test.java"); // creo un path verso un file

        // informazioni del path
        out.format("toString: %s%n", path.toString());
        out.format("getFileName: %s%n", path.getFileName());
        out.format("getName(0): %s%n", path.getName(0));
        out.format("getNameCount: %d%n", path.getNameCount());
        out.format("subpath(1, 2): %s%n", path.subpath(1, 2));
        out.format("getParent: %s%n", path.getParent());
        out.format("getRoot: %s%n", path.getRoot());
    }
}
