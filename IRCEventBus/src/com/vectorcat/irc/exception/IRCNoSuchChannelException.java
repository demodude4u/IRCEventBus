package com.vectorcat.irc.exception;

import java.io.IOException;

import com.vectorcat.irc.Channel;

public class IRCNoSuchChannelException extends IOException {
	private static final long serialVersionUID = 3026847078685062714L;

	private final Channel channel;

	public IRCNoSuchChannelException(Channel channel) {
		super("No such channel: " + channel);
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
