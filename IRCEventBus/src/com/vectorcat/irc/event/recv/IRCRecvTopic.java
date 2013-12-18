package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvTopic extends IRCRecvEvent {
	private final Channel channel;
	private final String topic;
	private final User user;
	private final long date;
	private final boolean changed;
}
