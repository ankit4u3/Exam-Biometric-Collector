/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;
import java.io.*;
import java.net.*;
import java.util.*;

public class ListNetsEx
{
    public static void main(String args[]) 
      throws SocketException 
    {
        Enumeration nets = 
            NetworkInterface.getNetworkInterfaces();
        for (Object netint : Collections.list(nets))
            displayInterfaceInformation((NetworkInterface) netint);
    }

    static void displayInterfaceInformation(NetworkInterface netint) 
      throws SocketException 
    {
        System.out.println("Display name: " 
           + netint.getDisplayName());
        System.out.println("Hardware address: " 
           + Arrays.toString(netint.getHardwareAddress()));
    }
} 