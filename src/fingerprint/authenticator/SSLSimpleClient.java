/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLSimpleClient {


  public static final int PORT = 9096;
   public static void main(String[] args) throws Exception {
      String host = "192.168.1.191";

      SocketFactory sf = SSLSocketFactory.getDefault();
      SSLSocket sock = (SSLSocket) sf.createSocket(host, PORT);
      System.out.println("Server connected");

      InputStream rawIn = sock.getInputStream( );
      BufferedReader in = new BufferedReader(new InputStreamReader(rawIn));
      System.out.println(in.readLine( ));
      sock.close() ;
   
  }
}