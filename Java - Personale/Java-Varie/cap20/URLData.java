package com.pellegrinoprincipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLData
{
    public static void main(String[] args) throws MalformedURLException, IOException
    {
        String res = "http://www.pellegrinoprincipe.com/JAVASCRIPT/elaJa_V0.1/about/index.html";
        URL url = new URL(res);

        URLConnection u_conn = url.openConnection();

        // ottengo varie info sul contenuto
        System.out.println("CONTENT ENCODING: " + u_conn.getContentEncoding()
                          + "\nCONTENT LENGTH: " + u_conn.getContentLength()
                          + "\nCONTENT TYPE: " + u_conn.getContentType());
        System.out.println("CONTENUTO: ");

        // leggo il contenuto della risorsa
        try (BufferedReader in = new BufferedReader(new InputStreamReader(u_conn.getInputStream())))
        {
            String data;
            while ((data = in.readLine()) != null)
                System.out.println(data);
        }
    }
}
