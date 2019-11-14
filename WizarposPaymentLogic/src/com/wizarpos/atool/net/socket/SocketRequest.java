package com.wizarpos.atool.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class SocketRequest {

	private Socket socket = null;

	private int timeout = 1000 * 10;

	private String address;

	private int port;

	public SocketRequest(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public void connect() throws InterruptedException {
		// wrap connecting in future for timeout
		FutureTask<Socket> futureTask = new FutureTask<Socket>(new Callable<Socket>() {
			public Socket call() throws Exception {
				// initialize socket
				Socket socket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(address, port);
				socket.connect(socketAddress);
				
				return socket;
			}

		});
		Thread executor = new Thread(futureTask);
		executor.start();

		// get connection to initiator with timeout
		try {
			socket = futureTask.get(timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {

		}
	}

	public InputStream getInputStream() throws IOException {
		return this.socket.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return this.socket.getOutputStream();
	}

	public int getReadTimeout() throws IOException {
		try {
			return this.socket.getSoTimeout();
		} catch (SocketException e) {
			throw new IOException("Error on underlying Socket");
		}
	}

	public void setReadTimeout(int timeout) throws IOException {
		try {
			this.socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			throw new IOException("Error on underlying Socket");
		}
	}
	
	public void close() throws IOException {
		this.socket.close();
	}

}
