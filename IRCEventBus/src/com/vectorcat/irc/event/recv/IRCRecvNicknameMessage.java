package com.vectorcat.irc.event.recv;

public class IRCRecvNicknameMessage extends IRCRecvMessage {

	public IRCRecvNicknameMessage(String nickname, String login,
			String hostname, String message) {
		super(nickname, nickname, login, hostname, message);
	}

}
