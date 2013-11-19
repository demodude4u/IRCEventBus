package com.vectorcat.irc;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.vectorcat.irc.event.IRCRecvEvent;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvServerResponse;
import com.vectorcat.irc.event.send.IRCSendJoin;
import com.vectorcat.irc.exception.IRCBadServerResponse;
import com.vectorcat.irc.exception.IRCNoSuchChannelException;
import com.vectorcat.irc.util.EventMonitor;
import com.vectorcat.irc.util.EventVisitor;

@Singleton
public class IRCControl {

	private final EventBus bus;
	private final IRCProtocol protocol;

	@Inject
	IRCControl(@Named("IRC") EventBus bus, IRCProtocol protocol) {
		this.bus = bus;
		this.protocol = protocol;
	}

	public void connect(String host, int port, String nickname, String password)
			throws UnknownHostException, IOException, IRCBadServerResponse {
		if (!protocol.isRunning()) {
			protocol.start();
		}
		protocol.connect(host, port, nickname, password);
	}

	public void disconnect() throws IOException {
		protocol.disconnect();
		if (protocol.isRunning()) {
			protocol.stop();
		}
	}

	public void join(final String channel) throws IOException,
			IRCNoSuchChannelException {
		try (EventMonitor<IRCRecvEvent> monitor = new EventMonitor<>(bus,
				IRCRecvEvent.class)) {

			bus.post(new IRCSendJoin(channel));

			monitor.pollEach(new EventVisitor<IRCRecvEvent>() {
				@Override
				public boolean visit(IRCRecvEvent event) throws IOException {
					if (event instanceof IRCRecvServerResponse) {
						IRCRecvServerResponse serverResponse = (IRCRecvServerResponse) event;
						if (serverResponse.getCode() == 403) {
							throw new IRCNoSuchChannelException(channel);
						}
					} else if (event instanceof IRCRecvJoin) {
						IRCRecvJoin join = (IRCRecvJoin) event;
						if (join.getChannel().toLowerCase()
								.equals(channel.toLowerCase())) {
							return true;
						}
					}
					return false;
				}
			}, 5, TimeUnit.SECONDS);
		}
	}

}
