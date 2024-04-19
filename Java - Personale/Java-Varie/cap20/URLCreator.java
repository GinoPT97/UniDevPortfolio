package com.pellegrinoprincipe;

import java.net.MalformedURLException;
import java.net.URL;

public class URLCreator
{
    public static void main(String args[]) throws MalformedURLException 
    {     
            URL a_url = new URL("http://www.pellegrinoprincipe.com:80/JAVASCRIPT/elaJa_V0.1/index.html");
            URL r_url = new URL(a_url, "about/index.html");
            URL param_url = new URL("http", "www.pellegrinoprincipe.com", "index.html");

            // ottieni le singole parti dell'URL
            System.out.println("[PROTOCOLLO: " + r_url.getProtocol()
                    + "] [HOST: " + r_url.getHost()
                    + "] [AUTHORITY: " + r_url.getAuthority()
                    + "] [PATH: " + r_url.getPath() + "]");
    }
}
