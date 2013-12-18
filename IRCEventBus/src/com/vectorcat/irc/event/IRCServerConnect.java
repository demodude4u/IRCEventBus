package com.vectorcat.irc.event;

import com.vectorcat.irc.IRCEvent;
import com.vectorcat.irc.Server;

public class IRCServerConnect extends IRCEvent {
	private final Server server;

	public IRCServerConnect(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
