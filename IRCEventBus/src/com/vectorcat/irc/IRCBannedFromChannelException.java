package com.vectorcat.irc;

import java.io.IOException;

public class IRCBannedFromChannelException extends IOException {
	private static final long serialVersionUID = 5350049018817064474L;

	private final Channel channel;

	public IRCBannedFromChannelException(Channel channel) {
		super("Banned from channel: " + channel);
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}
}
