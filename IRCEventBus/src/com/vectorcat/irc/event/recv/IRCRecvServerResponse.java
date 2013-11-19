package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvServerResponse extends IRCRecvEvent {

	private final int code;
	private final String response;

	public IRCRecvServerResponse(int code, String response) {
		this.code = code;
		this.response = response;
	}

	public int getCode() {
		return code;
	}

	public String getResponse() {
		return response;
	}

}
