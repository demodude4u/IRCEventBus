package com.vectorcat.irc.event.recv.mode;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvModeRemovePrivate extends IRCRecvEvent {
	private final Channel channel;
	private final User user;
	private final String login;
	private final String hostname;
}
