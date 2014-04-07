package com.redes.tarea1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor
{
	int puerto = 8080;

	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;

        void depura(String mensaje) // los mensajes por defecto serán en modo depuracion
	{
		depura(mensaje,DEBUG);
	} 

	// funcion para centralizar los mensajes de depuración
        	void depura(String mensaje, int gravedad)
        	{
        System.out.println("Mensaje: " + mensaje);
	} 

	// punto de entrada a nuestro programa
        public static void main(String [] array) 
	{
		Servidor instancia = new Servidor(array); 
		instancia.arranca();
	}

	// constructor que interpreta los parameros pasados
        Servidor(String[] param)
	{
		procesaParametros(); 
	}

	// parsearemos el fichero de entrada y estableceremos las variables de clase
        boolean procesaParametros()
	{
		return true; 
	}

        boolean arranca()
        {
        	depura("Arrancamos nuestro servidor",DEBUG);
        	try
        	{
        		ServerSocket s = new ServerSocket(90);
        		depura("Quedamos a la espera de conexion");
        		Socket entrante = s.accept();
        		depura("Procesamos conexion");
        		BufferedReader in = new BufferedReader (new InputStreamReader(entrante.getInputStream()));
        		String cadena = "";

        		while (cadena != null)
        		{
        			cadena = in.readLine();
        			if (cadena != null)
        			{
        				depura("--" + cadena);
        			}
        		}

        		depura("Hemos terminado");

        	}
        	catch(Exception e)
        	{
        		depura("Error en servidor\n" + e.toString());
        	}
        		return true;
        	}
        }
        	

