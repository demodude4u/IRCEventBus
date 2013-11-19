package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopic extends IRCRecvEvent {

	private final String channelName;
	private final String topic;
	private final String nickname;
	private final long date;
	private final boolean changed;

	public IRCRecvTopic(String channelName, String topic, String nickname,
			long date, boolean changed) {
		this.channelName = channelName;
		this.topic = topic;
		this.nickname = nickname;
		this.date = date;
		this.changed = changed;
	}

	public String getChannelName() {
		return channelName;
	}

	public long getDate() {
		return date;
	}

	public String getNickname() {
		return nickname;
	}

	public String getTopic() {
		return topic;
	}

	public boolean isChanged() {
		return changed;
	}

}
