package jf.comp5104.yahtzee;

@SuppressWarnings("serial")
public class InvalidGameStateException extends Exception {

	InvalidGameStateException() {
		super("Invalid game state.");
	}
}
