package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvMessage extends IRCRecvEvent {

	private final String login;
	private final String nickname;
	private final String hostname;
	private final String message;
	private final String target;

	public IRCRecvMessage(String target, String nickname, String login,
			String hostname, String message) {
		this.target = target;
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public String getTarget() {
		return target;
	}

}
