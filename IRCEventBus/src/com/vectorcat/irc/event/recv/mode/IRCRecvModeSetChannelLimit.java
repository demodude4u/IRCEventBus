package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeSetChannelLimit extends IRCRecvEvent {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final int limit;

	public IRCRecvModeSetChannelLimit(String channel, String nickname,
			String login, String hostname, int limit) {
		this.channel = channel;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.limit = limit;
	}

	public String getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public int getLimit() {
		return limit;
	}

	public String getLogin() {
		return login;
	}

	public String getNickname() {
		return nickname;
	}

}
