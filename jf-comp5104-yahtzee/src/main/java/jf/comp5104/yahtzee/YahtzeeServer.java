package jf.comp5104.yahtzee;

import java.net.*;
import java.io.*;


public class YahtzeeServer {
	
	
	public static void main(String[] args) {
		// Based on docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
		int portNumber = Integer.parseInt(args[0]);
		
		try (
				ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream() ));
				
				)
		{
			String inputLine, outputLine;
			
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
				if ("exit".equalsIgnoreCase(inputLine)) {
					break;
				}
			}
		}
		catch (IOException e) {
			// server socket threw an io exception
			System.out.println(e.toString());
			return;
		}
	}

}
