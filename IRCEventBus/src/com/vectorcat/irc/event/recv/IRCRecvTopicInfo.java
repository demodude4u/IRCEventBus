package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvTopicInfo extends IRCRecvEvent {

	private final String channelName;
	private final String nickname;
	private final long date;

	public IRCRecvTopicInfo(String channelName, String nickname, long date) {
		this.channelName = channelName;
		this.nickname = nickname;
		this.date = date;
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

}
