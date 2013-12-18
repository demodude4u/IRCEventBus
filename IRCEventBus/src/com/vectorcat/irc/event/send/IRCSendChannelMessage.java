package com.vectorcat.irc.event.send;

import com.vectorcat.irc.Channel;

public class IRCSendChannelMessage extends IRCSendMessage {

	private final Channel channel;

	public IRCSendChannelMessage(Channel channel, String message) {
		super(channel, message);
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
