package jf.comp5104.yahtzee.exceptions;

@SuppressWarnings("serial")
public class AlreadyScoredThereException extends Exception {

	public AlreadyScoredThereException() {
		super("Tried to score in a category which already has a score.");
	}
}
