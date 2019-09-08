package jf.comp5104.yahtzee;

import java.net.*;
import java.util.List;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class Yahtzee {

	private static final String DEFAULT_PORT = "3333";
	private static final String EOL = System.getProperty("line.separator");

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

		YahtzeeClient(String host, int port) {
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

	// Example command line stuff from
	// https://dzone.com/articles/java-command-line-interfaces-part-1-apache-commons

	private static void printHelp(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		final String syntax = "java -jar yahtzee.jar [-s] [host] [port]";
		final String usageHeader = "Yahtzee";
		final String usageFooter = "Now with Command Line Interface and Multiplayer!";
		System.out.println();
		System.out.println("====");
		System.out.println("HELP");
		System.out.println("====");
		formatter.printHelp(syntax, usageHeader, options, usageFooter);
	}

	public static void main(String[] args) {

		// create options for command line
		Options options = new Options();

		options.addOption("s", "server", false, "start server");
		options.addOption("h", "help", false, "command line help");
		// https://www.programcreek.com/java-api-examples/org.apache.commons.cli.OptionBuilder

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			List<String> argList = cmd.getArgList();
			// System.out.println("Args: " + argList);
			
			int port = Integer.parseInt(DEFAULT_PORT);
			String hostname = "localhost";

			if (cmd.hasOption("h")) {
				printHelp(options);
				return;
			}
			
			if (cmd.hasOption("s")) {
				if (argList.size() > 1) {
					printHelp(options);
					return;
				}
				if (!argList.isEmpty()) {
					port = Integer.parseInt(argList.iterator().next());
				}
				YahtzeeServer server = new YahtzeeServer(port);
				server.start();
			} else { // run as client by default
				if (argList.isEmpty() || argList.size() > 2) {
					printHelp(options);
					return;
				}
				if (argList.size() == 1) {
					hostname = argList.iterator().next();
				}
				if (argList.size() == 2) {
					hostname = argList.iterator().next();
					port = Integer.parseInt(argList.iterator().next());
				}
				YahtzeeClient client = new YahtzeeClient(hostname, port);
				client.connect();
			}

		} catch (ParseException e) {
			System.out.println(e.toString());
			System.err.println("Could not parse command line " + args);
			return;
		}	
	}
}
