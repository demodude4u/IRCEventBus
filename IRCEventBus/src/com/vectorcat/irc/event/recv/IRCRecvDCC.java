package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvDCC extends IRCRecvEvent {

	private final String nickname;
	private final String login;
	private final String hostname;
	private final String request;

	public IRCRecvDCC(String nickname, String login, String hostname,
			String request) {
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.request = request;
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

	public String getRequest() {
		return request;
	}
}
