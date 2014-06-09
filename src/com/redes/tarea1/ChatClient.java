package com.redes.tarea1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
 
import java.net.Socket;
import java.net.UnknownHostException;
 
public class ChatClient {
    private static int port = 8080; /* port to connect to */
    private static String host = "192.168.1.192"; /* host to connect to */
 
    private static BufferedReader stdIn;
 
    private static String nick;
    static String contactos;
    
    /**
     * Read in a nickname from stdin and attempt to authenticate with the 
     * server by sending a NICK command to @out. If the response from @in
     * is not equal to "OK" go back and read a nickname again
     */
    private static String getNick(BufferedReader in, 
            PrintWriter out) throws IOException { //login nick
	System.out.print("Enter your nick: ");
	String msg = stdIn.readLine(); //manda msg al server
	String usuarioExiste = "Cargando contactos de " + msg;
	out.println("NICK " + msg);
	String serverResponse = in.readLine();
	if ("SERVER: OK".equals(serverResponse)){
		//el usuario no existe, return usuario para login
		contactos = "0";
		//System.out.println("mensaje es " + msg);
		return msg; 
	}
	if ("SERVER: NICK IN USE".equals(serverResponse)) {        	
		System.out.println("Usuario esta logueado en sistema");      	
	}
	if(serverResponse.startsWith("SERVER: NICK EN ARCHIVO"))
	{ //return los contactos del usuario
		
		contactos = serverResponse.substring(23); //string con contactos sin parsear desde el server
		System.out.println("los contactos de " + msg + " son " + contactos); //return de los contactos del usuario
		System.out.println("OK");
		//System.out.println("mensaje es " + msg);
		return msg; //hace login y guarda contactos SIN PARSEAR en variable global
	}
	
	System.out.println(serverResponse);
	return getNick(in, out);
}
 
    public static void main (String[] args) throws IOException {
 
        Socket server = null;
 
        try {
            server = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.err.println(e);
            System.exit(1);
        }
 
        stdIn = new BufferedReader(new InputStreamReader(System.in));
 
        /* obtain an output stream to the server... */
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        /* ... and an input stream */
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
 
        nick = getNick(in, out);
 
        /* create a thread to asyncronously read messages from the server */
        ServerConn sc = new ServerConn(server);
        Thread t = new Thread(sc);
        t.start();
 
        String msg;
        /* loop reading messages from stdin and sending them to the server */
        while ((msg = stdIn.readLine()) != null) {
            out.println(msg);
        }
    }
}
 
class ServerConn implements Runnable {
    private BufferedReader in = null;
 
    public ServerConn(Socket server) throws IOException {
        /* obtain an input stream from the server */
        in = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
    }
 
    public void run() {
        String msg;
        try {
            /* loop reading messages from the server and show them 
             * on stdout */
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}