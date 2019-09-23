package jf.comp5104.yahtzee.net;

// encapsulates client and string message

public class Message {
	private TCPConnection sender;
	private String text;

	Message(TCPConnection c, String s) {
		this.sender = c;
		this.text = s;
	}

	public TCPConnection getSender() {
		return sender;
	}

	public String getText() {
		return text;
	}
	public String toString() {
		return text;
	}

}
