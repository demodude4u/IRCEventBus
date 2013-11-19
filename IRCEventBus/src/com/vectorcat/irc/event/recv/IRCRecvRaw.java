package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvRaw extends IRCRecvEvent {

	private final String message;

	public IRCRecvRaw(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
