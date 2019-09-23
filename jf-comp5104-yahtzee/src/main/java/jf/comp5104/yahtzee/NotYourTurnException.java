package jf.comp5104.yahtzee;

@SuppressWarnings("serial")
public class NotYourTurnException extends Exception {

	NotYourTurnException() {
		super("Inactive player tried to act");
	}
}
