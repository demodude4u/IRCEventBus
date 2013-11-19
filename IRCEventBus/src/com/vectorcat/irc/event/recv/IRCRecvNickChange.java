package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvNickChange extends IRCRecvEvent {

	private final String nickname;
	private final String login;
	private final String hostname;
	private final String newNickname;

	public IRCRecvNickChange(String nickname, String login, String hostname,
			String newNickname) {
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.newNickname = newNickname;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getNewNickname() {
		return newNickname;
	}

	public String getNickname() {
		return nickname;
	}

}
