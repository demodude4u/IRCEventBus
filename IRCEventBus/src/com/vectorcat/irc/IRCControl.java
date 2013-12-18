package com.vectorcat.irc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vectorcat.irc.event.IRCRecvEvent;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvServerResponse;
import com.vectorcat.irc.event.send.IRCSendJoin;
import com.vectorcat.irc.event.send.IRCSendMessage;
import com.vectorcat.irc.event.send.IRCSendPart;
import com.vectorcat.irc.exception.IRCBadServerResponse;
import com.vectorcat.irc.exception.IRCBannedFromChannelException;
import com.vectorcat.irc.exception.IRCNoSuchChannelException;
import com.vectorcat.irc.util.EventMonitor;
import com.vectorcat.irc.util.EventVisitor;

@Singleton
public class IRCControl {

	private final EventBus bus;
	private final IRCProtocol protocol;
	private final IRCHandles handles;

	@Inject
	IRCControl(EventBus bus, IRCProtocol protocol, IRCHandles handles) {
		this.bus = bus;
		this.protocol = protocol;
		this.handles = handles;
	}

	public void connectServer(Server server) throws UnknownHostException,
			IOException, IRCBadServerResponse {
		if (isConnected()) {
			try {
				disconnectServer();
			} catch (Exception e) {
				// We want to prevent previous connections causing problems
				// So just emit we saw something bad
				e.printStackTrace();
			}
		}
		protocol.startAsync();
		protocol.awaitRunning();
		protocol.connect(server);
	}

	public void disconnectServer() throws IOException {
		if (isConnected()) {
			protocol.disconnect();
			protocol.stopAsync();
			protocol.awaitTerminated();
		}
	}

	public Channel getChannel(String channel) {
		return handles.getChannel(channel);
	}

	public Server getConnectedServer() {
		Preconditions.checkState(isConnected(),
				"Cannot get connected server if not connected!");
		return protocol.getServer().orNull();
	}

	public Server getServer(String host, int port, String username,
			String password) {
		return handles.getServer(host, port, handles.getUser(username),
				password);
	}

	public Target getTarget(String target) {
		if (Target.isChannel(target)) {
			return getChannel(target);
		} else {
			return getUser(target);
		}
	}

	public User getUser(String user) {
		return handles.getUser(user);
	}

	public void ignore(String... targets) {
		for (String target : targets) {
			protocol.addIgnore(getTarget(target));
		}
	}

	public boolean isConnected() {
		return protocol.getServer().isPresent();
	}

	public void join(final Channel channel) throws IOException,
			IRCNoSuchChannelException, IRCBannedFromChannelException {
		try (EventMonitor<IRCRecvEvent> monitor = new EventMonitor<>(bus,
				IRCRecvEvent.class)) {

			bus.post(new IRCSendJoin(channel));

			monitor.pollEach(new EventVisitor<IRCRecvEvent>() {
				@Override
				public boolean visit(IRCRecvEvent event) throws IOException {
					if (event instanceof IRCRecvServerResponse) {
						IRCRecvServerResponse serverResponse = (IRCRecvServerResponse) event;
						if (serverResponse.getCode() == Codes.ERR_NOSUCHCHANNEL) {
							throw new IRCNoSuchChannelException(channel);
						} else if (serverResponse.getCode() == Codes.ERR_BANNEDFROMCHAN) {
							throw new IRCBannedFromChannelException(channel);
						}
					} else if (event instanceof IRCRecvJoin) {
						IRCRecvJoin join = (IRCRecvJoin) event;
						if (join.getChannel().equals(channel)) {
							return true;
						}
					}
					return false;
				}
			}, 30, TimeUnit.SECONDS);
		}
	}

	public void message(Target target, String message) {
		bus.post(new IRCSendMessage(target, message));
	}

	public void part(Channel channel) {
		bus.post(new IRCSendPart(channel));
	}
}
