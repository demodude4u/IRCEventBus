package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;

public class IRCRecvDirectedMessage extends IRCRecvMessage {

	private final String rawMessage;

	public IRCRecvDirectedMessage(Target target, User user, String login,
			String hostname, String rawMessage, String message) {
		super(target, user, login, hostname, message);
		this.rawMessage = rawMessage;
	}

	public String getRawMessage() {
		return rawMessage;
	}

}
