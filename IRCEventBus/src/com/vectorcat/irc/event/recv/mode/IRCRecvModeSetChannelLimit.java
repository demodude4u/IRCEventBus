package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeSetChannelLimit extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
	private final int limit;

	public IRCRecvModeSetChannelLimit(Channel channel, User user, String login,
			String hostname, int limit) {
		this.channel = channel;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.limit = limit;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public int getLimit() {
		return limit;
	}

	public String getLogin() {
		return login;
	}

	public User getUser() {
		return user;
	}

}
