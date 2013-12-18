package com.vectorcat.irc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.IRCEvent;
import com.vectorcat.irc.Server;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCServerConnect extends IRCEvent {
	private final Server server;
}
