package com.vectorcat.irc.event.recv;

public class IRCRecvCommandMessage extends IRCRecvMessage {

	private final String command;
	private final boolean isNicknameMessage;

	public IRCRecvCommandMessage(String target, String nickname, String login,
			String hostname, String message, String command,
			boolean isNicknameMessage) {
		super(target, nickname, login, hostname, message);
		this.command = command;
		this.isNicknameMessage = isNicknameMessage;
	}

	public String getCommand() {
		return command;
	}

	public boolean isNicknameMessage() {
		return isNicknameMessage;
	}

}
