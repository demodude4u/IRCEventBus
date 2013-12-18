package com.vectorcat.irc.event;

import com.vectorcat.irc.IRCEvent;
import com.vectorcat.irc.Server;

public class IRCServerDisconnect extends IRCEvent {
	private final Server server;

	public IRCServerDisconnect(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
