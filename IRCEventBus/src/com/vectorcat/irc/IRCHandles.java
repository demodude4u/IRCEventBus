package com.vectorcat.irc;

import com.google.inject.assistedinject.Assisted;

interface IRCHandles {

	public Channel getChannel(String channel);

	public Server getServer(@Assisted("host") String host, int port, User user,
			@Assisted("password") String password);

	public User getUser(String user);

}
