package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvInvite extends IRCRecvEvent {

	private final Target target;
	private final User user;
	private final String login;
	private final String hostname;
	private final String substring;

	public IRCRecvInvite(Target target, User user, String login,
			String hostname, String substring) {
		this.target = target;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.substring = substring;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getSubstring() {
		return substring;
	}

	public Target getTarget() {
		return target;
	}

	public User getUser() {
		return user;
	}

}
