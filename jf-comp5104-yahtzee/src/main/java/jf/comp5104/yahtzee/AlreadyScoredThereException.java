package jf.comp5104.yahtzee;

@SuppressWarnings("serial")
public class AlreadyScoredThereException extends Exception {

	AlreadyScoredThereException() {
		super("Tried to score in a category which already has a score.");
	}
}
