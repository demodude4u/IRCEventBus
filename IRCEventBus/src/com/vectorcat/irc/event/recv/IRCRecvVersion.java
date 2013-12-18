package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvVersion extends IRCRecvEvent {

	private final User user;
	private final String login;
	private final String hostname;
	private final Target target;

	public IRCRecvVersion(User user, String login, String hostname,
			Target target) {
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.target = target;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public Target getTarget() {
		return target;
	}

	public User getUser() {
		return user;
	}

}
