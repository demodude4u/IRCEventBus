package com.vectorcat.irc.feature;

import java.util.Map;

import com.beust.jcommander.internal.Maps;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vectorcat.irc.event.recv.IRCRecvCommandMessage;
import com.vectorcat.irc.event.send.IRCSendMessage;

public class CleverBotFeature {

	private final EventBus bus;

	private final Map<String, ChatterBotSession> sessions = Maps.newHashMap();

	private final ChatterBot chatterBot;

	@Inject
	CleverBotFeature(@Named("IRC") EventBus bus) throws Exception {
		this.bus = bus;
		bus.register(this);

		ChatterBotFactory factory = new ChatterBotFactory();
		chatterBot = factory.create(ChatterBotType.JABBERWACKY);
	}

	private ChatterBotSession getSession(String identity) {
		ChatterBotSession ret = sessions.get(identity);
		if (ret == null) {
			sessions.put(identity, ret = chatterBot.createSession());
		}
		return ret;
	}

	@Subscribe
	public void onCommand(IRCRecvCommandMessage event) throws Exception {
		String say = event.getCommand();

		ChatterBotSession session = getSession(event.getNickname()
				.toLowerCase());

		String response = session.think(say);
		if (!event.isNicknameMessage()) {
			response = event.getNickname() + ": " + response;
		}

		bus.post(new IRCSendMessage(event.getTarget(), response));
	}
}
