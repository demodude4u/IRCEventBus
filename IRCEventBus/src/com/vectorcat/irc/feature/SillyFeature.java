package com.vectorcat.irc.feature;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vectorcat.irc.IRCProtocol;
import com.vectorcat.irc.event.recv.IRCRecvChannelMessage;
import com.vectorcat.irc.event.send.IRCSendMessage;

public class SillyFeature {

	private final EventBus bus;
	private final IRCProtocol protocol;

	@Inject
	SillyFeature(@Named("IRC") EventBus bus, IRCProtocol protocol) {
		this.bus = bus;
		this.protocol = protocol;
		bus.register(this);
	}

	@Subscribe
	public void onEchoFaggot(IRCRecvChannelMessage event) {
		if (event.getNickname().equals("domeD")) {
			bus.post(new IRCSendMessage(event.getChannel(), "!RACECAR!"));
		}
	}

	@Subscribe
	public void onInkrementSayingMyName(IRCRecvChannelMessage event) {
		String lowerNickname = protocol.getMyNickname().toLowerCase();
		String lowerMessage = event.getMessage().toLowerCase();
		if (event.getNickname().equals("Inkrement")
				&& lowerMessage.contains(lowerNickname)) {
			bus.post(new IRCSendMessage(event.getChannel(),
					"I'm not listening to you! :D"));
		}
	}

}
