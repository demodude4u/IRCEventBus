package com.vectorcat.irc;

import java.io.IOException;
import java.net.UnknownHostException;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vectorcat.irc.exception.IRCBadServerResponse;

public class Server {

	private final IRCControl control;
	private final String host;
	private final int port;
	private final User user;
	private final String password;

	@Inject
	Server(IRCControl control, @Assisted("host") String host,
			@Assisted int port, @Assisted User user,
			@Assisted("password") String password) {
		this.control = control;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public void connect() throws UnknownHostException, IRCBadServerResponse,
			IOException {
		control.connectServer(this);
	}

	public void disconnect() throws IOException {
		control.disconnectServer();
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public User getUser() {
		return user;
	}
}
