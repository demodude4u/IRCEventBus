package com.vectorcat.irc;

import java.util.Collection;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vectorcat.irc.event.IRCServerConnect;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvNickChange;
import com.vectorcat.irc.event.recv.IRCRecvPart;

@Singleton
public class IRCState {
	private class Subscriber {
		@Subscribe
		public void onConnect(IRCServerConnect event) {
			myServer = event.getServer();
			myUser = myServer.getUser();
		}

		@Subscribe
		public void onJoin(IRCRecvJoin event) {
			channelUsers.put(event.getChannel(), event.getUser());
		}

		@Subscribe
		public void onNick(IRCRecvNickChange event) {
			if (event.getUser().equals(getMyUser())) {
				myUser = event.getNewUser();
			}
			System.out.println("New Nickname! " + myUser);
		}

		@Subscribe
		public void onPart(IRCRecvPart event) {
			channelUsers.remove(event.getChannel(), event.getUser());
			if (event.getUser().equals(getMyUser())) {
				channelUsers.removeAll(event.getChannel());
			}
		}
	}

	private final Multimap<Channel, User> channelUsers = LinkedHashMultimap
			.create();

	private User myUser;
	private Server myServer;

	@Inject
	IRCState(EventBus bus) {
		bus.register(new Subscriber());
	}

	public Server getMyServer() {
		return myServer;
	}

	public User getMyUser() {
		return myUser;
	}

	public Collection<User> getUsers(Channel channel) {
		return channelUsers.get(channel);
	}

	public boolean inChannel(Channel channel, User user) {
		return getUsers(channel).contains(user);
	}
}
