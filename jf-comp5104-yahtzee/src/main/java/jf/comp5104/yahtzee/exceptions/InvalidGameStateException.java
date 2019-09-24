package jf.comp5104.yahtzee.exceptions;

@SuppressWarnings("serial")
public class InvalidGameStateException extends Exception {

	InvalidGameStateException() {
		super("Invalid game state.");
	}
}
