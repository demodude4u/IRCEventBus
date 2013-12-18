package com.vectorcat.irc.event.send;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendJoin extends IRCSendEvent {

	private final Channel channel;

	public IRCSendJoin(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String getRawMessage() {
		return "JOIN " + channel;
	}

}
