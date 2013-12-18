package com.vectorcat.irc.event;

import com.vectorcat.irc.IRCEvent;

public abstract class IRCSendEvent extends IRCEvent {
	public abstract String getRawMessage();
}
