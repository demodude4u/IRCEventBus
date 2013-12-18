package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvUserMessage extends IRCRecvMessage {

	public IRCRecvUserMessage(User user, String login, String hostname,
			String message) {
		super(user, user, login, hostname, message);
	}
}
