package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvInvite extends IRCRecvEvent {

	private final String target;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String substring;

	public IRCRecvInvite(String target, String nickname, String login,
			String hostname, String substring) {
		this.target = target;
		this.nickname = nickname;
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

	public String getNickname() {
		return nickname;
	}

	public String getSubstring() {
		return substring;
	}

	public String getTarget() {
		return target;
	}

}
