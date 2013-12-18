package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvMode extends IRCRecvEvent {

	private final Target target;
	private final User user;
	private final String login;
	private final String hostname;
	private final String mode;

	public IRCRecvMode(Target target, User user, String login, String hostname,
			String mode) {
		this.target = target;
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

	public Target getTarget() {
		return target;
	}

	public User getUser() {
		return user;
	}

}
