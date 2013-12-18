package com.vectorcat.irc.event.recv.mode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvModeSetChannelKey extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
	private final String key;

	public IRCRecvModeSetChannelKey(Channel channel, User user, String login,
			String hostname, String key) {
		this.channel = channel;
		this.user = user;
		this.login = login;
		this.hostname = hostname;
		this.key = key;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getHostname() {
		return hostname;
	}

	public String getKey() {
		return key;
	}

	public String getLogin() {
		return login;
	}

	public User getUser() {
		return user;
	}

}
