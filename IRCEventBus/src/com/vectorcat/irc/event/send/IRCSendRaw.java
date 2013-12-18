package com.vectorcat.irc.event.send;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.event.IRCSendEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCSendRaw extends IRCSendEvent {
	private final String rawMessage;
}
