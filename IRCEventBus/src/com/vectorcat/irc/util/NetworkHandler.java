package com.vectorcat.irc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkHandler {

	private volatile Socket socket = null;

	public void connect(String host, int port) throws UnknownHostException,
			IOException {
		disconnect();
		socket = new Socket(InetAddress.getByName(host), port);
	}

	public void disconnect() throws IOException {
		if (socket != null) {
			socket.close();
		}
		socket = null;
	}

	public InputStream getInputStream() {
		return new InputStream() {
			Socket socket = null;
			private InputStream inputStream;

			@Override
			public int read() {
				while (true) {
					try {
						if (socket != NetworkHandler.this.socket) {
							socket = NetworkHandler.this.socket;
							if (socket == null) {
								Thread.sleep(100);
								continue;
							}
							inputStream = socket.getInputStream();
						}
						if (socket == null) {
							Thread.sleep(100);
							continue;
						}
						int ret = inputStream.read();
						if (ret == -1) {
							Thread.yield();
							continue;
						}
						return ret;
					} catch (Exception e) {
						e.printStackTrace();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
	}

	public OutputStream getOutputStream() {
		return new OutputStream() {
			Socket socket = null;
			private OutputStream outputStream;

			@Override
			public void write(int b) {
				while (true) {
					try {
						if (socket != NetworkHandler.this.socket) {
							socket = NetworkHandler.this.socket;
							if (socket == null) {
								Thread.sleep(100);
								continue;
							}
							outputStream = socket.getOutputStream();
						}
						outputStream.write(b);
						return;
					} catch (Exception e) {
						// e.printStackTrace();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
	}

}
