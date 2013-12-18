package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.User;

public class IRCRecvUserMessage extends IRCRecvMessage {

	public IRCRecvUserMessage(User user, String login, String hostname,
			String message) {
		super(user, user, login, hostname, message);
	}

}
