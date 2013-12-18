package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvServerResponse extends IRCRecvEvent {
	private final int code;
	private final String response;
}
