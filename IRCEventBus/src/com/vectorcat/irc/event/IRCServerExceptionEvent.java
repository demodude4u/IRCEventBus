package com.vectorcat.irc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.IRCEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCServerExceptionEvent extends IRCEvent {
	private final Exception exception;
}
