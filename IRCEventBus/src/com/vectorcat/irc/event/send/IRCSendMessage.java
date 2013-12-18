package com.vectorcat.irc.event.send;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.event.IRCSendEvent;

public class IRCSendMessage extends IRCSendEvent {

	private final Target target;
	private final String message;

	public IRCSendMessage(Target target, String message) {
		this.target = target;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String getRawMessage() {
		return "PRIVMSG " + target.toString() + " :" + message;
	}

	public Target getTarget() {
		return target;
	}

}
