package com.vectorcat.irc;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class User extends Target {

	private final IRCState state;

	@Inject
	User(IRCControl control, IRCState state, @Assisted String user) {
		super(control, user);
		this.state = state;
	}

	@Override
	@Deprecated
	public Channel asChannel() {
		throw new IllegalStateException();
	}

	public Collection<Channel> getChannels() {
		return state.getChannels(this);
	}

	public boolean isVisible() {
		return state.isVisible(this);
	}

}
