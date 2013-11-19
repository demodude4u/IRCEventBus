package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvKick extends IRCRecvEvent {

	private final String target;
	private final String nickname;
	private final String login;
	private final String hostname;
	private final String recipient;
	private final String message;

	public IRCRecvKick(String target, String nickname, String login,
			String hostname, String recipient, String message) {
		this.target = target;
		this.nickname = nickname;
		this.login = login;
		this.hostname = hostname;
		this.recipient = recipient;
		this.message = message;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public String getMessage() {
		return message;
	}

	public String getNickname() {
		return nickname;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getTarget() {
		return target;
	}

}
