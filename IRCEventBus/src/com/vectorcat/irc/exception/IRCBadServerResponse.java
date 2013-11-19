package com.vectorcat.irc.exception;

import java.io.IOException;

import com.vectorcat.irc.event.recv.IRCRecvServerResponse;

public class IRCBadServerResponse extends IOException {
	private static final long serialVersionUID = -5968327500754620771L;

	private final IRCRecvServerResponse event;

	public IRCBadServerResponse(IRCRecvServerResponse event) {
		super("Bad Server Response (" + event.getCode() + "): "
				+ event.getResponse());
		this.event = event;
	}

	public IRCRecvServerResponse getEvent() {
		return event;
	}

}
