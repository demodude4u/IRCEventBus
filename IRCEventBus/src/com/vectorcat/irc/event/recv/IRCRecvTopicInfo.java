package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopicInfo extends IRCRecvEvent {

	private final Channel channel;
	private final User user;
	private final long date;

	public IRCRecvTopicInfo(Channel channel, User user, long date) {
		this.channel = channel;
		this.user = user;
		this.date = date;
	}

	public Channel getChannel() {
		return channel;
	}

	public long getDate() {
		return date;
	}

	public User getUser() {
		return user;
	}

}
