package com.vectorcat.irc;

import java.util.Collection;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.vectorcat.irc.event.IRCServerConnect;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvNameReply;
import com.vectorcat.irc.event.recv.IRCRecvNickChange;
import com.vectorcat.irc.event.recv.IRCRecvPart;
import com.vectorcat.irc.event.recv.IRCRecvQuit;
import com.vectorcat.irc.event.recv.IRCRecvRaw;

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
			addToChannel(event.getChannel(), event.getUser());
		}

		@Subscribe
		// Happens on my channel join
		public void onNameReply(IRCRecvNameReply event) {
			for (User user : event.getUsersAndPrefixes().keySet()) {
				addToChannel(event.getChannel(), user);
				System.out.println("Name Reply: " + user);
			}
		}

		@Subscribe
		public void onNick(IRCRecvNickChange event) {
			if (event.getUser().equals(getMyUser())) {
				myUser = event.getNewUser();
				System.out.println("New Nickname! " + myUser);
			}
			renameUser(event.getUser(), event.getNewUser());
		}

		@Subscribe
		public void onPart(IRCRecvPart event) {
			removeFromChannel(event.getChannel(), event.getUser());
		}

		@Subscribe
		public void onQuit(IRCRecvQuit event) {
			removeUser(event.getUser());
		}

		@Subscribe
		public void onRecvRaw(IRCRecvRaw event) {
			lastRecvTimeMillis = System.currentTimeMillis();
		}
	}

	private long lastRecvTimeMillis = System.currentTimeMillis();

	private final Multimap<Channel, User> channelUsers = LinkedHashMultimap
			.create();
	private final Multimap<User, Channel> userChannels = LinkedHashMultimap
			.create();

	private User myUser;
	private Server myServer;

	@Inject
	IRCState(@Named("recvBus") EventBus bus) {
		bus.register(new Subscriber());
	}

	private void addToChannel(Channel channel, User user) {
		channelUsers.put(channel, user);
		userChannels.put(user, channel);
	}

	public Collection<Channel> getChannels() {
		return channelUsers.keySet();
	}

	public Collection<Channel> getChannels(User user) {
		return userChannels.get(user);
	}

	public long getLastRecvTimeMillis() {
		return lastRecvTimeMillis;
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

	public boolean isVisible(User user) {
		return !userChannels.get(user).isEmpty();
	}

	private void removeFromChannel(Channel channel, User user) {
		channelUsers.remove(channel, user);
		userChannels.remove(user, channel);
		if (user.equals(getMyUser())) {
			for (User otherUser : channelUsers.removeAll(channel)) {
				userChannels.remove(otherUser, channel);
			}
		}
	}

	private void removeUser(User user) {
		for (Channel channel : userChannels.get(user)) {
			Collection<User> users = channelUsers.get(channel);
			users.remove(user);
		}
		userChannels.removeAll(user);
	}

	private void renameUser(User user, User newUser) {
		for (Channel channel : userChannels.get(user)) {
			Collection<User> users = channelUsers.get(channel);
			users.remove(user);
			users.add(newUser);
			userChannels.get(newUser).add(channel);
		}
		userChannels.removeAll(user);
	}
}
