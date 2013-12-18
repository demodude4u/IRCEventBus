package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvChannelInfo extends IRCRecvEvent {
	private final Channel channel;
	private final int userCount;
	private final String topic;
}
