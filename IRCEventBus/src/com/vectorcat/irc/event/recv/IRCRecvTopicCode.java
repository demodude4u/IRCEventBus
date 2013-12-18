package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopicCode extends IRCRecvEvent {

	private final Channel channel;
	private final String topic;

	public IRCRecvTopicCode(Channel channel, String topic) {
		this.channel = channel;
		this.topic = topic;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getTopic() {
		return topic;
	}

}
