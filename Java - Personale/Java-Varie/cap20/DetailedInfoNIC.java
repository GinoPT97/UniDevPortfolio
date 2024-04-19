
package com.pellegrinoprincipe;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.net.InterfaceAddress;
import java.util.Arrays;
import java.net.NetworkInterface;
import java.net.SocketException;
import static java.lang.System.out;

public class DetailedInfoNIC
{
    public static void main(String args[])
    {
        try
        {
            NetworkInterface nic = NetworkInterface.getByName("eth3"); // ritorna la NIC con il nome indicato
            if (nic != null)
            {
                out.println("Nome NIC: " + nic.getDisplayName());
                
                byte ott[] =  nic.getHardwareAddress(); // MAC address
                out.format("Mac address: %02X-%02X-%02X-%02X-%02X-%02X\n", ott[0], ott[1], ott[2], ott[3], ott[4], ott[5]);
                
                out.println("E' UP ? " + nic.isUp());
                out.println("E' una sub-interfaccia ? " + nic.isVirtual());

                List<InterfaceAddress> ia = nic.getInterfaceAddresses(); // mostra gli indirizzi associati alla NIC
                for (InterfaceAddress one_ia : ia)
                {
                    InetAddress addr = one_ia.getAddress();
                    String ip = addr.getHostAddress();

                    int prefix = one_ia.getNetworkPrefixLength();
                    boolean ipv4 = addr instanceof Inet4Address;

                    out.println("IP: " + ip + "/" + prefix);
                    if (ipv4)
                    {
                        String broadcast_addr = one_ia.getBroadcast().getHostAddress();
                        out.println("Broadcast address: " + broadcast_addr);
                    }
                }
            }
            else
                out.print("NIC non trovata!");
        }
        catch (SocketException ex) { System.out.println(ex.getMessage()); }
    }
}

