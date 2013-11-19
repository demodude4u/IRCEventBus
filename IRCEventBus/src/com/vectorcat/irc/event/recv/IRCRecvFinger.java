package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvFinger extends IRCRecvEvent {

	private final String nickname;
	private final String login;
	private final String hostname;
	private final String target;

	public IRCRecvFinger(String nickname, String login, String hostname,
			String target) {
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public String getTarget() {
		return target;
	}

}
