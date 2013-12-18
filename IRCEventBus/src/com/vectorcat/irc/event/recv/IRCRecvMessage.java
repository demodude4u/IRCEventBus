package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvMessage extends IRCRecvEvent {

	private final String login;
	private final User user;
	private final String hostname;
	private final String message;
	private final Target target;

	public IRCRecvMessage(Target target, User user, String login,
			String hostname, String message) {
		this.target = target;
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

	public Target getTarget() {
		return target;
	}

	public User getUser() {
		return user;
	}

}
