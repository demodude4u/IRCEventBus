package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvEndOfNames extends IRCRecvEvent {

	private final String channelName;

	public IRCRecvEndOfNames(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

}
