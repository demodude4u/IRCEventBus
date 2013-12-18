package com.vectorcat.irc.event.send;

import com.vectorcat.irc.User;

public class IRCSendNicknameMessage extends IRCSendMessage {

	private final User user;

	public IRCSendNicknameMessage(User user, String message) {
		super(user, message);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
