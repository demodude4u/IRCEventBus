package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;

public class IRCRecvChannelMessage extends IRCRecvMessage {

	private final Channel channel;

	public IRCRecvChannelMessage(Channel channel, User user, String login,
			String hostname, String message) {
		super(channel, user, login, hostname, message);
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
