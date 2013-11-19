package com.vectorcat.irc.event.recv.mode;

public class IRCRecvModeRemoveChannelBan {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String hostmask;

	public IRCRecvModeRemoveChannelBan(String channel, String nickname,
			String login, String hostname, String hostmask) {
		this.channel = channel;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.hostmask = hostmask;
	}

	public String getChannel() {
		return channel;
	}

	public String getHostmask() {
		return hostmask;
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
