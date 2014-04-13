package com.redes.tarea1;

import java.io.*;
import java.net.* ;
import java.util.*;

class webServer {
    static public void main(String[] args) throws Exception {
    	parsing("nombre=potato&ip=1.1.1.1&puerto=6789");

    }
	 

	static void parsing (String query)
	{
		String result[] = new String[9];
	    int i=0;
	    for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        	result[i]=pair[0];
	        	result[i+1]=pair[1];		
	        	i+=2;
	        	//System.out.println(pair[0] + " : " + pair[1]);
	        }
        System.out.println(result[0] + " " + result[1] + " " + result[2] + " " + result[3] + " " + result[4] + " " + result[5]);

	        }
	    
	}
