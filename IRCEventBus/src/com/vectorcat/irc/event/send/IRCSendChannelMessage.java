package com.vectorcat.irc.event.send;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.vectorcat.irc.Channel;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRCSendChannelMessage extends IRCSendMessage {
	private final Channel channel;

	public IRCSendChannelMessage(Channel channel, String message) {
		super(channel, message);
		this.channel = channel;
	}
}
