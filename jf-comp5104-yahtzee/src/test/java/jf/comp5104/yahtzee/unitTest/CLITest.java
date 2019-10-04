package jf.comp5104.yahtzee.unitTest;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.Test;

import jf.comp5104.yahtzee.Yahtzee;
import jf.comp5104.yahtzee.net.YahtzeeServer;

// https://www.developer.com/tech/article.php/3669436/test-driving-a-java-command-line-application.htm
public class CLITest {
	private static final String EOL = System.getProperty("line.separator");

	@Test
	public void testShowHelpWhenInsufficientArgumentsSupplied() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream console = System.out;
		try {
			System.setOut(new PrintStream(bytes));
			Yahtzee.main(new String[] {});
		} finally {
			System.setOut(console);
		}
		String helpMessage = String.format(
				"%n" + "====%n" + "HELP%n" + "====%n" + "usage: java -jar yahtzee.jar [-s] [host] [port]%n"
						+ "Yahtzee%n" + " -h,--help     command line help%n" + " -s,--server   start server%n"
						+ "Now with Command Line Interface and Multiplayer!%n",
				EOL, EOL, EOL, EOL, EOL, EOL, EOL, EOL, EOL);
		assertEquals(helpMessage, bytes.toString());
	}

	public void testLaunchServerWithoutPort() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream console = System.out;
		try {
			System.setOut(new PrintStream(bytes));
			Yahtzee.main(new String[] { "-s" });
		} finally {
			System.setOut(console);
		}
		String launchMessage = String.format("Server listening on port %s%n", YahtzeeServer.DEFAULT_PORT, EOL);
		assertEquals(launchMessage, bytes.toString());
	}
	
	public void testLaunchServerWithPort() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		PrintStream console = System.out;
		try {
			System.setOut(new PrintStream(bytes));
			Yahtzee.main(new String[] { "-s 4242" });
		} finally {
			System.setOut(console);
		}
		String launchMessage = String.format("Server listening on port 4242%n", EOL);
		assertEquals(launchMessage, bytes.toString());
	}
}
