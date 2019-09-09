package jf.comp5104.yahtzee;

import jf.comp5104.yahtzee.net.*;

import java.net.*;
import java.util.List;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.*;


public class Yahtzee {

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
			
			int port = Integer.parseInt(YahtzeeServer.DEFAULT_PORT);
			String hostname = YahtzeeServer.DEFAULT_HOST;

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
