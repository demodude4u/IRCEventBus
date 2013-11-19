package com.vectorcat.irc.event.recv.mode;

public class IRCRecvModeSetSecret {

	private final String channel;
	private final String nickname;
	private final String login;
	private final String hostname;

	public IRCRecvModeSetSecret(String channel, String nickname,
			String login, String hostname) {
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
