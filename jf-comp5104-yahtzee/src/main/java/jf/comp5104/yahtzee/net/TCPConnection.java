package jf.comp5104.yahtzee.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

// Concrete String (PrintWriter/BufferedReader) implementation of AbstractSession
public class TCPConnection {
 
	private String hostname;
	private int port;
	private Socket socket;
	//private InputStream din;
	//private OutputStream dout;
	private PrintWriter out;
	private BufferedReader in;
	UUID id;
	private InetAddress INetAddress;


	public TCPConnection(String hostname, int port) throws IOException {
		this(new Socket(hostname, port));		
	}

	public TCPConnection(Socket socket) throws IOException {
		try {
			this.socket = socket;
			//this.din = socket.getInputStream();
			//this.dout = socket.getOutputStream();
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e) {
			System.err.println("Couldn't load TCP socket for some reason.");
			System.err.println(e.getMessage());
			throw e;
		}
		this.setINetAddress(socket.getInetAddress());
		id = UUID.randomUUID();
		
	}

	public Socket getSocket() {
		return socket;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int p) {
		this.port = p;
	}

	public UUID getId() {
		return id;
	}

	public void send(String str) {
		System.out.println(this.toString());
		System.out.println("Connection send()");
		out.println(str);
	}

	public String receive() {
		String s;
		try {
			System.out.println(this.toString());
			System.out.println("Connection receive()");
			s = in.readLine();
		}
		catch (IOException e) {
			s = "";
			System.err.println("Unable to read from socket");
		}
		return s;
	}

	public InetAddress getINetAddress() {
		return INetAddress;
	}

	public void setINetAddress(InetAddress iNetAddress) {
		INetAddress = iNetAddress;
	}

	public String toString() {
		return this.getHostname() + " " + this.getPort() + " " + this.id;
	}
}
