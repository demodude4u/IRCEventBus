package com.vectorcat.irc.event.send;

import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendJoin extends IRCSendEvent {

	private final String channel;

	public IRCSendJoin(String channel) {
		this.channel = channel;
	}

	@Override
	public String getRawMessage() {
		return "JOIN " + channel;
	}

}
