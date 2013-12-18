package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvKick extends IRCRecvEvent {

	private final Target target;
	private final User user;
	private final String login;
	private final String hostname;
	private final String recipient;
	private final String message;

	public IRCRecvKick(Target target, User user, String login, String hostname,
			String recipient, String message) {
		this.target = target;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.recipient = recipient;
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

	public String getRecipient() {
		return recipient;
	}

	public Target getTarget() {
		return target;
	}

	public User getUser() {
		return user;
	}

}
