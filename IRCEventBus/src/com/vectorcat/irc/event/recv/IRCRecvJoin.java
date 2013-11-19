package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvJoin extends IRCRecvEvent {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;

	public IRCRecvJoin(String channel, String nickname, String login,
			String hostname) {
		this.channel = channel;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
	}

	public String getChannel() {
		return channel;
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

}
