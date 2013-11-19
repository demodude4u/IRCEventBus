package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeSetChannelKey extends IRCRecvEvent {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String key;

	public IRCRecvModeSetChannelKey(String channel, String nickname,
			String login, String hostname, String key) {
		this.channel = channel;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.key = key;
	}

	public String getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public String getKey() {
		return key;
	}

	public String getLogin() {
		return login;
	}

	public String getNickname() {
		return nickname;
	}

}
