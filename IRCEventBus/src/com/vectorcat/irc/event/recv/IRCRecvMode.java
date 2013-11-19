package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvMode extends IRCRecvEvent {

	private final String target;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String mode;

	public IRCRecvMode(String target, String nickname, String login,
			String hostname, String mode) {
		this.target = target;
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public String getTarget() {
		return target;
	}

}
