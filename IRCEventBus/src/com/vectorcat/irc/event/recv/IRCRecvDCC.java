package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvDCC extends IRCRecvEvent {

	private final User user;
	private final String login;
	private final String hostname;
	private final String request;

	public IRCRecvDCC(User user, String login, String hostname,
			String request) {
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.request = request;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public User getUser() {
		return user;
	}

	public String getRequest() {
		return request;
	}
}
