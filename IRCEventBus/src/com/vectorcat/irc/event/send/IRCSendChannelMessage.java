package com.vectorcat.irc.event.send;

public class IRCSendChannelMessage extends IRCSendMessage {

	public IRCSendChannelMessage(String channel, String message) {
		super(channel, message);
	}

	public String getChannel() {
		return getTarget();
	}

}
