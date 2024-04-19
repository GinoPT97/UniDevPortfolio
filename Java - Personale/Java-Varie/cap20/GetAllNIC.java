package com.pellegrinoprincipe;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

public class GetAllNIC
{
    public static void main(String args[])
    {
        try
        {
            // dammi le NIC presenti
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            if (nics != null)
            {
                while (nics.hasMoreElements())
                {
                    NetworkInterface nic = nics.nextElement();
                    System.out.println("Nome NIC: " + nic.getDisplayName() + 
                                       "( " + nic.getName() + " )");

                    Enumeration<NetworkInterface> sub_nics = nic.getSubInterfaces();// ha delle sottointerfacce?

                    if (sub_nics != null)
                    {
                        for (NetworkInterface sub_nic : Collections.list(sub_nics))
                            System.out.println("\tNome SUB-NIC: " + sub_nic.getDisplayName()  + 
                                               "( " + sub_nic.getName() + " )");
                    }
                    else
                        System.out.println("Non sono presenti sub-interfacce!");
                }
            }
            else
                System.out.println("Nessuna interfaccia di rete trovata!");
        }
        catch (SocketException ex){ System.out.println(ex.getMessage()); }
    }
}
