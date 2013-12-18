package com.vectorcat.irc.event.send;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Channel;
import com.vectorcat.irc.event.IRCSendEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCSendJoin extends IRCSendEvent {

	private final Channel channel;

	@Override
	public String getRawMessage() {
		return "JOIN " + channel;
	}
}
