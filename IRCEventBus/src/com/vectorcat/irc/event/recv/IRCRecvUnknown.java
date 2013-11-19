package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvUnknown extends IRCRecvEvent {

	private final String message;

	public IRCRecvUnknown(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
