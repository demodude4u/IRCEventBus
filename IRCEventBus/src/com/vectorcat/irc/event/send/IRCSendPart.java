package com.vectorcat.irc.event.send;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendPart extends IRCSendEvent {

	private final Channel channel;

	public IRCSendPart(Channel channel) {
		this.channel = channel;
	}

	@Override
	public String getRawMessage() {
		return "PART " + channel.toString();
	}

}
