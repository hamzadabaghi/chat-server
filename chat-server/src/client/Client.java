package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		System.out.println("je me connecte au serveur");
		
		Socket socket = new Socket("localhost",1234);
		
		// input chaine de caractere 
		
		InputStream is = socket.getInputStream();		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		// output chaine de caractere avec le buffer desactive
		OutputStream os = socket.getOutputStream();
		PrintWriter pw = new PrintWriter(os,true);
		
				
		System.out.println(br.readLine());

		Scanner scanner = new Scanner(System.in);
		
		System.out.println("\npour le message : tapez un message , sauf pour un client specifique  : code_client=>message \n");
		System.out.println("tapez un message");
		while(true)
		{	
	
		String message = scanner.nextLine();
		pw.println(message);
		String reponse = br.readLine();
		System.out.println(reponse);
		
		}
		
		
		
	}

}
