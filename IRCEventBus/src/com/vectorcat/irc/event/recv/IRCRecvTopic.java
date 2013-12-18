package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopic extends IRCRecvEvent {

	private final Channel channel;
	private final String topic;
	private final User user;
	private final long date;
	private final boolean changed;

	public IRCRecvTopic(Channel channel, String topic, User user, long date,
			boolean changed) {
		this.channel = channel;
		this.topic = topic;
		this.user = user;
		this.date = date;
		this.changed = changed;
	}

	public Channel getChannel() {
		return channel;
	}

	public long getDate() {
		return date;
	}

	public String getTopic() {
		return topic;
	}

	public User getUser() {
		return user;
	}

	public boolean isChanged() {
		return changed;
	}

}
