package com.vectorcat.irc.event.recv;

public class IRCRecvCommandMessage extends IRCRecvMessage {

	private final String command;

	public IRCRecvCommandMessage(String target, String nickname, String login,
			String hostname, String message, String command) {
		super(target, nickname, login, hostname, message);
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

}
