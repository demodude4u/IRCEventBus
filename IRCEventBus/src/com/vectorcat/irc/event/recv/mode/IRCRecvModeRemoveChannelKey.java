package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeRemoveChannelKey extends IRCRecvEvent {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String recipient;

	public IRCRecvModeRemoveChannelKey(String channel, String nickname, String login,
			String hostname, String recipient) {
		this.channel = channel;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.recipient = recipient;
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

	public String getRecipient() {
		return recipient;
	}

}