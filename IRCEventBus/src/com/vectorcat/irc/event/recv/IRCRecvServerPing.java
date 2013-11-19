package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvServerPing extends IRCRecvEvent {

	private final String response;

	public IRCRecvServerPing(String response) {
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

}
