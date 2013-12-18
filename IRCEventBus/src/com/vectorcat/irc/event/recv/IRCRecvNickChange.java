package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvNickChange extends IRCRecvEvent {

	private final User user;
	private final String login;
	private final String hostname;
	private final User newUser;

	public IRCRecvNickChange(User user, String login, String hostname,
			User newUser) {
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.newUser = newUser;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public User getNewUser() {
		return newUser;
	}

	public User getUser() {
		return user;
	}

}
