package com.vectorcat.irc;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class User extends Target {

	@Inject
	User(IRCControl control, @Assisted String user) {
		super(control, user);
	}

	@Override
	@Deprecated
	public Channel asChannel() {
		throw new IllegalStateException();
	}

}
