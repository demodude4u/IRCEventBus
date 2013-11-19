package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvAction extends IRCRecvEvent {

	private final String nickname;
	private final String login;
	private final String hostname;
	private final String target;
	private final String message;

	public IRCRecvAction(String nickname, String login, String hostname,
			String target, String message) {
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.target = target;
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

	public String getNickname() {
		return nickname;
	}

	public String getTarget() {
		return target;
	}

}
