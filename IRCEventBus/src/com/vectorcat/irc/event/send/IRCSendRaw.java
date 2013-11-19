package com.vectorcat.irc.event.send;

import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendRaw extends IRCSendEvent {

	private final String rawMessage;

	public IRCSendRaw(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	@Override
	public String getRawMessage() {
		return rawMessage;
	}

}
