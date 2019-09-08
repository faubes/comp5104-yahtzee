package jf.comp5104.yahtzee;

import java.net.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.*;

public class Yahtzee {

	public static class YahtzeeServer {
		int portNumber;

		YahtzeeServer(int port) {
			portNumber = port;
		}

		int getPort() {
			return portNumber;
		}

		void start() {

			// docs.oracle.com/javase/tutorial/networking/sockets/clientServer.html
			System.out.println("Start Server.");
			try (ServerSocket serverSocket = new ServerSocket(getPort());
					Socket clientSocket = serverSocket.accept();
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

				System.out.println("Created server socket on port " + getPort());

				String inputLine, outputLine;

				while ((inputLine = in.readLine()) != null) {
					System.out.println(inputLine);
					if ("exit".equalsIgnoreCase(inputLine)) {
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

	public static class YahtzeeClient {
		int portNumber;
		String hostName = "localhost";

		YahtzeeClient(int port) {
			portNumber = port;
		}

		void connect() {
			try (Socket socket = new Socket(hostName, portNumber);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
				System.out.println("Connected to server?");
				String userInput;
				while ((userInput = stdIn.readLine()) != null) {
					out.println(userInput);
					System.out.println("echo: " + in.readLine());
				}

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		Options options = new Options();

		options.addOption("s", "server", true, "run a CLI Yahtzee server");
		options.addOption("c", "client", true, "run a CLI Yahtzee client");
		// options.addOption("p", "port", 4356, "port (default 4356 for no
		// reason)");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("s")) {
				YahtzeeServer server = new YahtzeeServer(Integer.parseInt(cmd.getOptionValue("s")));
			}
			if (cmd.hasOption("c")) {
				YahtzeeClient client = new YahtzeeClient(Integer.parseInt(cmd.getOptionValue("c")));
			}
		} catch (ParseException e) {
			System.out.println(e.toString());
			System.err.println("Could not parse command line " + args);
			return;
		}
	}
}
