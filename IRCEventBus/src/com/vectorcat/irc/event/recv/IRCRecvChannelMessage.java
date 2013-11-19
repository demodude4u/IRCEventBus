package com.vectorcat.irc.event.recv;

public class IRCRecvChannelMessage extends IRCRecvMessage {

	private final String channel;

	public IRCRecvChannelMessage(String channel, String nickname, String login,
			String hostname, String message) {
		super(channel, nickname, login, hostname, message);
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

}
