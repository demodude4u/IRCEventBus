package com.vectorcat.irc.event.recv;

import com.google.common.collect.ImmutableMap;
import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvNameReply extends IRCRecvEvent {

	private final Channel channel;
	private final ImmutableMap<String, String> nicknamesAndPrefixes;

	public IRCRecvNameReply(Channel channel,
			ImmutableMap<String, String> nicknamesAndPrefixes) {
		this.channel = channel;
		this.nicknamesAndPrefixes = nicknamesAndPrefixes;
	}

	public Channel getChannel() {
		return channel;
	}

	public ImmutableMap<String, String> getNicknamesAndPrefixes() {
		return nicknamesAndPrefixes;
	}

}
