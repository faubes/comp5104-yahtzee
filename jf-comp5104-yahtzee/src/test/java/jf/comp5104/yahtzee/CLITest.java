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
         Yahtzee.main(null);
      } finally {
         System.setOut(console);
      }
      assertEquals(String.format(
            "too few arguments%n" +
            "Client: java -jar Yahtzee -c remote port%n" +
            "Server: java -jar Yahtzee -s port%n", EOL, EOL, EOL),
            bytes.toString());
   }
}