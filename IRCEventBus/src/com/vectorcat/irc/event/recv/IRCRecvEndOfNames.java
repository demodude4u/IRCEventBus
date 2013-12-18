package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvEndOfNames extends IRCRecvEvent {

	private final Channel channel;

	public IRCRecvEndOfNames(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
