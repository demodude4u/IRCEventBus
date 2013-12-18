package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvQuit extends IRCRecvEvent {

	private final User user;
	private final String login;
	private final String hostname;
	private final String message;

	public IRCRecvQuit(User user, String login, String hostname,
			String message) {
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.message = message;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

}
