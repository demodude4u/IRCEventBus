package com.vectorcat.irc.event.send;

public class IRCSendNicknameMessage extends IRCSendMessage {

	public IRCSendNicknameMessage(String nickname, String message) {
		super(nickname, message);
	}

	public String getNickname() {
		return getTarget();
	}

}
