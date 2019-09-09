package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

public class YahtzeeClient {
	int portNumber;
	String hostName;

	public YahtzeeClient(String host, int port) {
		portNumber = port;
		hostName = host;
	}

	public void connect() {
		try (Socket socket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
			System.out.println("Connected to server " + hostName + " on port " + portNumber);

			String fromServer;
			String userInput;
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);

				if ("Bye".equalsIgnoreCase(fromServer)) {
					break;
				}

				userInput = stdIn.readLine();
				if (StringUtils.isNotBlank(userInput)) {
					out.println(userInput);
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Disconnected.");
	}
}
