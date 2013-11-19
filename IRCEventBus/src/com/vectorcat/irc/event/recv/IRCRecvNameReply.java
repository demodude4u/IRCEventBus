package com.vectorcat.irc.event.recv;

import com.google.common.collect.ImmutableMap;
import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvNameReply extends IRCRecvEvent {

	private final String channelName;
	private final ImmutableMap<String, String> nicknamesAndPrefixes;

	public IRCRecvNameReply(String channelName,
			ImmutableMap<String, String> nicknamesAndPrefixes) {
		this.channelName = channelName;
		this.nicknamesAndPrefixes = nicknamesAndPrefixes;
	}

	public String getChannelName() {
		return channelName;
	}

	public ImmutableMap<String, String> getNicknamesAndPrefixes() {
		return nicknamesAndPrefixes;
	}

}
