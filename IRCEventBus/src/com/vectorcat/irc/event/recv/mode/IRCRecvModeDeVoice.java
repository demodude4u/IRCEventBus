package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeDeVoice extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
	private final User recipient;

	public IRCRecvModeDeVoice(Channel channel, User user, String login,
			String hostname, User recipient) {
		this.channel = channel;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.recipient = recipient;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public User getRecipient() {
		return recipient;
	}

	public User getUser() {
		return user;
	}

}
