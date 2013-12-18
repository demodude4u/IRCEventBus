package com.vectorcat.irc;

public abstract class Target {

	public static boolean isChannel(String target) {
		return Channel.CHANNEL_PREFIXES.indexOf(toIdentifier(target).charAt(0)) != -1;
	}

	public static boolean isUser(String target) {
		return !isChannel(target);
	}

	public static String toIdentifier(String target) {
		return target.trim().toLowerCase();
	}

	private final IRCControl control;
	private final String target;

	private final String identifier;

	protected Target(IRCControl control, String target) {
		this.control = control;
		this.target = target;
		identifier = toIdentifier(target);
	}

	public Channel asChannel() {
		return (Channel) this;
	}

	public User asUser() {
		return (User) this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Target other = (Target) obj;
		if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	public boolean equalsString(String target) {
		return getIdentifier().equals(toIdentifier(target));
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	public boolean isChannel() {
		return (this instanceof Channel);
	}

	public boolean isUser() {
		return (this instanceof User);
	}

	public void message(String message) {
		control.message(this, message);
	}

	@Override
	public String toString() {
		return target;
	}

}
