package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeVoice extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
	private final String recipient;

	public IRCRecvModeVoice(Channel channel, User user, String login,
			String hostname, String recipient) {
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

	public String getRecipient() {
		return recipient;
	}

	public User getUser() {
		return user;
	}

}
