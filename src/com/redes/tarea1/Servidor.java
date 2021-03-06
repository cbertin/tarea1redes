package com.redes.tarea1;

import java.io.*;
import java.net.*;
import java.util.*;
//import java.util.stream.Stream;

public class Servidor
{
	int puerto = 8080;
	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;
	String line;
	String result[] = new String[8];

	  
	// funcion para centralizar los mensajes de depuracion
	void depura(String mensaje)
	{
		depura(mensaje,DEBUG);
	}	

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
		
			
			ServerSocket s = new ServerSocket(puerto);

			depura("Quedamos a la espera de conexion");
			
			while(true)  // bucle infinito .... ya veremos como hacerlo de otro modo
			{
				Socket entrante = s.accept();
				peticionWeb pCliente = new peticionWeb(entrante);
				pCliente.start();
			}
			
		}
		catch(Exception e)
		{
			depura("Error en servidor\n" + e.toString());
		}
		
		return true;
	}
	
	public void escribir(String nombreArchivo,String texto)
	{
	File f;
	f = new File(nombreArchivo);
	//Escritura
	try{
		FileWriter w = new FileWriter(f,true);
		BufferedWriter bw = new BufferedWriter(w);
		PrintWriter wr = new PrintWriter(bw);  
		wr.println(texto);//escribimos en el archivo
		wr.close();
		bw.close();
		}catch(IOException e)
		{};
	}
	
void parsing(String query) //metodo parseo de querry
{
   int i=0;
   int inicio;
   inicio = query.indexOf("nick");
   query = query.substring(inicio);
   for (String param : query.split("&")) {
       String pair[] = param.split("=");
        result[i]=pair[0];
        result[i+1]=pair[1];  
        i+=2;
        //System.out.println(pair[0] + " : " + pair[1]);
       }
   		for(int j=0;j<result.length;j++)
   			System.out.println("-- " + result[j] + "--");
   		
   		//escribir("contacto.pp", "nombre="+result[1]+"&ip="+result[3]+"&puerto="+result[5]);

   }



class peticionWeb extends Thread
{
	int contador = 0;

	final int ERROR = 0;
	final int WARNING = 1;
	final int DEBUG = 2;

	void depura(String mensaje)
	{
		depura(mensaje,DEBUG);
	}	

	void depura(String mensaje, int gravedad)
	{
		//System.out.println(currentThread().toString() + " - " + mensaje);
	}	

	private Socket scliente 	= null;		// representa la petici�n de nuestro cliente
   	private PrintWriter out 	= null;		// representa el buffer donde escribimos la respuesta

   	peticionWeb(Socket ps)
   	{
		depura("El contador es " + contador);
		
		contador ++;
		
		
		
		scliente = ps;
		setPriority(NORM_PRIORITY - 1); // hacemos que la prioridad sea baja
   	}

	public void run() // emplementamos el metodo run
	{
		depura("Procesamos conexion");
		int endFile=0;


		try
		{
			BufferedReader in = new BufferedReader (new InputStreamReader(scliente.getInputStream()));
  			out = new PrintWriter(new OutputStreamWriter(scliente.getOutputStream(),"8859_1"),true) ;


			String cadena = "";		// cadena donde almacenamos las lineas que leemos
			int i=0;				// lo usaremos para que cierto codigo solo se ejecute una vez
	
			do			
			{
				cadena = in.readLine();
				System.out.println("CAPTADO:"+cadena);

				if (cadena != null )
				{
					// sleep(500);
					depura("--" + cadena + "-");
				}


				if(i == 0) // la primera linea nos dice que fichero hay que descargar
				{
			        i++;
			       
			       StringTokenizer st = new StringTokenizer(cadena);
			       String tok = st.nextToken();

                    if ((st.countTokens() >= 2) && tok.equals("GET")) 
                    {
                    	retornaFichero(st.nextToken()) ; //aqui desarmamos la peticion 
                    	System.out.println("dentro de GET");
                    	
                    }

                	if((st.countTokens() >= 2) && tok.equals("POST")){
                		String checkEnd="";
                		System.out.println("dentro de POST\n");      
                		int pepe;
                		StringBuilder concadenado = new StringBuilder(); //para concatenar el string, aun con null entre medio
                		System.out.println("endFile="+endFile);
                		while(endFile==0){ //en form agregado char 178
                			pepe=in.read();
                			concadenado.append((char)pepe);
                		//System.out.print("\n---CONCADENADO---\n " + concadenado.toString() + "\n---END OF CONCADENADO---\n");
                		System.out.print((char)pepe); //%EF%BF%BD%3
                		
                		checkEnd = concadenado.toString();                		
                			if(checkEnd.endsWith("%EF%BF%BD")){ //si el string include la �
                				endFile=1;
                				System.out.println("\nSALIR DEL WHILE!!!!!!!\n");
                				parsing(checkEnd);
                    			//close socket?                				
                			}
                			}
                		System.out.println("POST:CAPTADO:"+checkEnd);
                		retornaFichero("chat.htm");//le responde al servidor devolviendo la vista luego de agregar el contacto
                		}

                		}
                		
                		               		
                	
                	else
                    {
                    	out.println("400 Petici�n Incorrecta") ;
                    }
				}
				
			
			while (cadena != null && cadena.length() != 0);

		}
		catch(Exception e)
		{
			depura("Error en servidor\n" + e.toString());
		}
			
		depura("Hemos terminado");
	}
	
	
	void retornaFichero(String sfichero) throws IOException
	{
		depura("Recuperamos el fichero " + sfichero);
		System.out.println("El fichero es "+sfichero);
		// comprobamos si tiene una barra al principio
		if (sfichero.startsWith("/"))
		{
			sfichero = sfichero.substring(1) ;
		}
        
        // si acaba en /, le retornamos el index.htm de ese directorio
        // si la cadena esta vacia, no retorna el index.htm principal
        if (sfichero.endsWith("/") || sfichero.equals(""))
        {
        	sfichero = sfichero + "login.htm" ;
        }

        String variables="";
        if (sfichero.equals("chat.htm"))
        {
        	variables = "juanca cele pepe groar";//Aqui tenemos que hacer el enlace con el servidor CLIENTETCP
		        	
        }
        
        try
        {
	        
		    // Ahora leemos el fichero y lo retornamos
		    File mifichero = new File(sfichero) ;
		        
		    if (mifichero.exists()) 
		    {
	      		out.println("HTTP/1.0 200 ok");
				//out.println("Server: Tarea1 Server/1.0");
				out.println("Date: " + new Date());
				out.println("Content-Type: text/html");
				//out.println("Content-Length: " + mifichero.length());
				out.println("Datos:"+variables);
				out.println("\n");
				BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
				String linea = "";
				do			
				{
					linea = ficheroLocal.readLine();
	
					if (linea != null )
					{
						// sleep(500);
						out.println(linea);
					}
				}
				while (linea != null);
				
				depura("fin envio fichero");
				
				ficheroLocal.close();
				out.close();
				
			}  // fin de si el fiechero existe 
			else
			{
				depura("No encuentro el fichero " + mifichero.toString());	
	      		out.println("HTTP/1.0 400 ok");
	      		out.close();
			}
			
		}
		catch(Exception e)
		{
			depura("Error al retornar fichero");	
		}

	}
	}
}

	
