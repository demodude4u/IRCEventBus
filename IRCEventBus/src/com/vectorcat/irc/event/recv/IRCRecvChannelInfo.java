package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvChannelInfo extends IRCRecvEvent {

	private final String channel;
	private final int userCount;
	private final String topic;

	public IRCRecvChannelInfo(String channel, int userCount, String topic) {
		this.channel = channel;
		this.userCount = userCount;
		this.topic = topic;
	}

	public String getChannel() {
		return channel;
	}

	public String getTopic() {
		return topic;
	}

	public int getUserCount() {
		return userCount;
	}

}
