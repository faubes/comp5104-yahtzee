package jf.comp5104.yahtzee;

public class InvalidGameStateException extends Exception {

	InvalidGameStateException() {
		super("Invalid game state.");
	}
}
