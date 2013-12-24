package com.vectorcat.irc.event.recv;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.common.collect.ImmutableMap;
import com.vectorcat.irc.Channel;
import com.vectorcat.irc.User;
import com.vectorcat.irc.event.IRCRecvEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCRecvNameReply extends IRCRecvEvent {
	private final Channel channel;
	private final ImmutableMap<User, String> usersAndPrefixes;
}
