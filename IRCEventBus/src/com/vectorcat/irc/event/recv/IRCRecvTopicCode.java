package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopicCode extends IRCRecvEvent {

	private final String channelName;
	private final String topic;

	public IRCRecvTopicCode(String channelName, String topic) {
		this.channelName = channelName;
		this.topic = topic;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getTopic() {
		return topic;
	}

}
