package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;

public class IRCRecvModeRemoveChannelBan {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
	private final String hostmask;

	public IRCRecvModeRemoveChannelBan(Channel channel, User user,
			String login, String hostname, String hostmask) {
		this.channel = channel;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.hostmask = hostmask;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getHostmask() {
		return hostmask;
	}

	public String getHostname() {
		return hostname;
	}

	public String getLogin() {
		return login;
	}

	public User getNickname() {
		return user;
	}
}
