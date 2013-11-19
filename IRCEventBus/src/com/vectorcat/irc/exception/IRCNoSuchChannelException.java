package com.vectorcat.irc.exception;

import java.io.IOException;

public class IRCNoSuchChannelException extends IOException {
	private static final long serialVersionUID = 3026847078685062714L;

	private final String channel;

	public IRCNoSuchChannelException(String channel) {
		super("No such channel: " + channel);
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

}
