package jf.comp5104.yahtzee.net;

import java.net.Socket;
import java.util.UUID;

public interface AbstractSession {
	Socket getSocket();
	UUID getId();
	void send(Object o);
	Object receive();
}
