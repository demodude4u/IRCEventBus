package com.vectorcat.irc.event.recv;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvCommand extends IRCRecvEvent {
	private final Target target;
	private final User user;
	private final String login;
	private final String hostname;
	private final String rawMessage;
	private final String message;
	private final boolean directedAtMe;
	private final String command;
	private final List<String> arguments;

	public String getArgumentString() {
		StringBuilder builder = new StringBuilder();
		for (String argument : arguments) {
			builder.append(argument + " ");
		}
		return builder.toString();
	}
}
