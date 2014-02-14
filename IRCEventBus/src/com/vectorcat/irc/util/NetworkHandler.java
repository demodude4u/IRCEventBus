package com.vectorcat.irc.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Predicate;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NetworkHandler {

	private volatile Socket socket = null;

	private Predicate<Exception> exceptionHandler = null;

	@Inject
	public NetworkHandler() {
	}

	public synchronized void connect(String host, int port)
			throws UnknownHostException, IOException {
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
			private volatile Socket socket;
			private volatile InputStream inputStream;

			@Override
			public int read() throws IOException {
				while (true) {
					try {
						if (socket != NetworkHandler.this.socket) {
							socket = NetworkHandler.this.socket;
							if (socket == null) {
								Uninterruptibles.sleepUninterruptibly(100,
										TimeUnit.MILLISECONDS);
								continue;
							}
							inputStream = socket.getInputStream();
						}
						if (socket == null) {
							Uninterruptibles.sleepUninterruptibly(100,
									TimeUnit.MILLISECONDS);
							continue;
						}
						int ret = inputStream.read();
						if (ret == -1) {
							throw new EOFException();
						}
						return ret;
					} catch (IOException e) {
						disconnect();
						if (exceptionHandler != null) {
							exceptionHandler.apply(e);
						}
					}
				}
			}
		};
	}

	public OutputStream getOutputStream() {
		return new OutputStream() {
			private volatile Socket socket;
			private volatile OutputStream outputStream;

			@Override
			public void write(int b) throws IOException {
				while (true) {
					try {
						if (socket != NetworkHandler.this.socket) {
							socket = NetworkHandler.this.socket;
							if (socket == null) {
								Uninterruptibles.sleepUninterruptibly(1,
										TimeUnit.SECONDS);
								continue;
							}
							outputStream = socket.getOutputStream();
						}
						outputStream.write(b);
						return;
					} catch (IOException e) {
						disconnect();
						if (exceptionHandler != null) {
							exceptionHandler.apply(e);
						}
					}
				}
			}
		};
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected() && !socket.isClosed();
	}

	public void setExceptionHandler(Predicate<Exception> exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
