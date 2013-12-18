package com.vectorcat.irc.event.send;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.event.IRCSendEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCSendMessage extends IRCSendEvent {
	private final Target target;
	private final String message;

	@Override
	public String getRawMessage() {
		return "PRIVMSG " + target.toString() + " :" + message;
	}
}
