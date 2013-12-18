package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvUserMode extends IRCRecvEvent {

	private final User recipient;
	private final User user;
	private final String login;
	private final String hostname;
	private final String mode;

	public IRCRecvUserMode(User recipient, User user, String login,
			String hostname, String mode) {
		this.recipient = recipient;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.mode = mode;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getMode() {
		return mode;
	}

	public User getRecipient() {
		return recipient;
	}

	public User getUser() {
		return user;
	}
}
