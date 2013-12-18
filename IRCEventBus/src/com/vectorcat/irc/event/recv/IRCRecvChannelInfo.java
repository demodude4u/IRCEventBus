package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvChannelInfo extends IRCRecvEvent {

	private final Channel channel;
	private final int userCount;
	private final String topic;

	public IRCRecvChannelInfo(Channel channel, int userCount, String topic) {
		this.channel = channel;
		this.userCount = userCount;
		this.topic = topic;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getTopic() {
		return topic;
	}

	public int getUserCount() {
		return userCount;
	}

}
