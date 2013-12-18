package com.vectorcat.irc;

import java.io.IOException;
import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.vectorcat.irc.exception.IRCNoSuchChannelException;

public class Channel extends Target {

	private final IRCState state;
	private final IRCControl control;
	static final String CHANNEL_PREFIXES = "#&+!";

	@Inject
	Channel(IRCState state, IRCControl control, @Assisted String channel) {
		super(control, channel);
		this.state = state;
		this.control = control;
	}

	@Override
	@Deprecated
	public User asUser() {
		throw new IllegalStateException();
	}

	public Collection<User> getUsers() {
		return state.getUsers(this);
	}

	public boolean inChannel() {
		return state.inChannel(this, state.getMyUser());
	}

	public boolean inChannel(User user) {
		return state.inChannel(this, user);
	}

	public void join() throws IRCNoSuchChannelException, IOException {
		control.join(this);
	}

	public void part() {
		control.part(this);
	}
}
