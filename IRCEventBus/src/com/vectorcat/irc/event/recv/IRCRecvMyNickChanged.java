package com.vectorcat.irc.event.recv;

import com.vectorcat.irc.event.IRCRecvEvent;

public class IRCRecvMyNickChanged extends IRCRecvEvent {

	private final String oldNickname;
	private final String newNickname;

	public IRCRecvMyNickChanged(String oldNickname, String newNickname) {
		this.oldNickname = oldNickname;
		this.newNickname = newNickname;
	}

	public String getNewNickname() {
		return newNickname;
	}

	public String getOldNickname() {
		return oldNickname;
	}

}
