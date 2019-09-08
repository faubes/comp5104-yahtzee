package jf.comp5104.yahtzee;

import java.net.*;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class Yahtzee {

	static class YahtzeeServer {
		int portNumber;

		YahtzeeServer(int port) {
			portNumber = port;
		}

		int getPort() {
			return portNumber;
		}

		void start() {
			int port = getPort();
			// docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
			// docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html

			// add logging eventually?

			System.out.println("Start Server on port " + port);
			
			try (ServerSocket serverSocket = new ServerSocket(port);
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

				System.out.println("Created server socket on port " + port);

				String inputLine, outputLine;

				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
					if ("exit".equalsIgnoreCase(inputLine)) {
						out.println("Bye");
						break;
					}
				}
			} catch (IOException e) {
				// server socket threw an io exception
				System.out.println(e.toString());
				return;
			}
		}
	}

	static class YahtzeeClient {
		int portNumber;
		String hostName;

		YahtzeeClient(int port, String host) {
			portNumber = port;
			hostName = host;
		}

		void connect() {
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

	private static final String DEFAULT_PORT = "3333";

	public static void main(String[] args) {
		
		// create options for command line
		Options options = new Options();

		options.addOption("s", "server", false, "start a server");
		options.addOption("c", "client", false, "connect to server (default)");
		options.addOption("h", "hostname", true, "server address (default localhost)");
		
		// https://www.programcreek.com/java-api-examples/org.apache.commons.cli.OptionBuilder
		
		@SuppressWarnings("static-access")
		Option portOption = Option.builder("p").withArgName("port number").hasArg()
				.withDescription("port number of the Web server. Defaults to " + DEFAULT_PORT)
				.create("port");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("s")) {
				YahtzeeServer server = new YahtzeeServer(Integer.parseInt(cmd.getOptionValue("s")));
				server.start();
			}
			if (cmd.hasOption("c") && cmd.hasOption("h")) {

				YahtzeeClient client = new YahtzeeClient(Integer.parseInt(cmd.getOptionValue("c")),
						cmd.getOptionValue("h"));
				client.connect();
			}
		} catch (ParseException e) {
			System.out.println(e.toString());
			System.err.println("Could not parse command line " + args);
			return;
		}
	}
}
