package jf.comp5104.yahtzee;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.Test;

// https://www.developer.com/tech/article.php/3669436/test-driving-a-java-command-line-application.htm
public class CLITest {
   private static final String EOL =
      System.getProperty("line.separator");

   @Test
   public void testShowUsageWhenInsufficientArgumentsSupplied() {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      PrintStream console = System.out;
      try {
         System.setOut(new PrintStream(bytes));
         Yahtzee.main(new String[] {});
      } finally {
         System.setOut(console);
      }
      String helpMessage = String.format(
    		  "%n" +
    		  "====%n" +
    		  "HELP%n" +
    		  "====%n" +
    		  "usage: java -jar yahtzee.jar [-s] [host] [port]%n" +
    		  "Yahtzee%n" +
    		  " -h,--help     command line help%n" +
    		  " -s,--server   start server%n" +
    		  "Now with Command Line Interface and Multiplayer!%n",
    		  EOL, EOL, EOL, EOL, EOL, EOL, EOL, EOL, EOL);
      assertEquals(helpMessage,
            bytes.toString());
   }
}