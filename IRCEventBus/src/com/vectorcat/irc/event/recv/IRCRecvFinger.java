package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Target;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvFinger extends IRCRecvEvent {
	private final User user;
	private final String login;
	private final String hostname;
	private final Target target;
}
