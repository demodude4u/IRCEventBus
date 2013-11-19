package com.vectorcat.irc.event.send;

import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendMessage extends IRCSendEvent {

	private final String target;
	private final String message;

	public IRCSendMessage(String target, String message) {
		this.target = target;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String getRawMessage() {
		return "PRIVMSG " + target + " :" + message;
	}

	public String getTarget() {
		return target;
	}

}
