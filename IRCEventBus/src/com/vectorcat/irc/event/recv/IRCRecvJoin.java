package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvJoin extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;

	public IRCRecvJoin(Channel channel, User user, String login, String hostname) {
		this.channel = channel;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
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

	public User getUser() {
		return user;
	}

}
