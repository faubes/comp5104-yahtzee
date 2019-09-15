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
public class TCPConnection implements AbstractSession {
 
	private String hostname;
	private int port;
	private Socket socket;
	private InputStream din;
	private OutputStream dout;
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
			this.din = socket.getInputStream();
			this.dout = socket.getOutputStream();
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
	@Override
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

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void send(Object o) {
		out.println(o.toString());
	}

	@Override
	public Object receive() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(in.readLine());
		}
		catch (IOException e) {
			System.err.println("Unable to read from socket");
		}

		return (Object)sb.toString();
	}

	public InetAddress getINetAddress() {
		return INetAddress;
	}

	public void setINetAddress(InetAddress iNetAddress) {
		INetAddress = iNetAddress;
	}

}
