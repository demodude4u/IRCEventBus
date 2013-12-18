package com.vectorcat.irc.event.send;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCSendUserMessage extends IRCSendMessage {
	private final User user;

	public IRCSendUserMessage(User user, String message) {
		super(user, message);
		this.user = user;
	}

}
