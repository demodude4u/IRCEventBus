package com.vectorcat.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.vectorcat.irc.event.IRCSendEvent;
import com.vectorcat.irc.event.recv.IRCRecvAction;
import com.vectorcat.irc.event.recv.IRCRecvChannelInfo;
import com.vectorcat.irc.event.recv.IRCRecvChannelMessage;
import com.vectorcat.irc.event.recv.IRCRecvCommandMessage;
import com.vectorcat.irc.event.recv.IRCRecvDCC;
import com.vectorcat.irc.event.recv.IRCRecvEndOfNames;
import com.vectorcat.irc.event.recv.IRCRecvFinger;
import com.vectorcat.irc.event.recv.IRCRecvInvite;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvKick;
import com.vectorcat.irc.event.recv.IRCRecvMessage;
import com.vectorcat.irc.event.recv.IRCRecvMode;
import com.vectorcat.irc.event.recv.IRCRecvMyNickChanged;
import com.vectorcat.irc.event.recv.IRCRecvNameReply;
import com.vectorcat.irc.event.recv.IRCRecvNickChange;
import com.vectorcat.irc.event.recv.IRCRecvNicknameMessage;
import com.vectorcat.irc.event.recv.IRCRecvNotice;
import com.vectorcat.irc.event.recv.IRCRecvPart;
import com.vectorcat.irc.event.recv.IRCRecvPing;
import com.vectorcat.irc.event.recv.IRCRecvQuit;
import com.vectorcat.irc.event.recv.IRCRecvRaw;
import com.vectorcat.irc.event.recv.IRCRecvServerPing;
import com.vectorcat.irc.event.recv.IRCRecvServerResponse;
import com.vectorcat.irc.event.recv.IRCRecvTime;
import com.vectorcat.irc.event.recv.IRCRecvTopic;
import com.vectorcat.irc.event.recv.IRCRecvTopicCode;
import com.vectorcat.irc.event.recv.IRCRecvTopicInfo;
import com.vectorcat.irc.event.recv.IRCRecvUnknown;
import com.vectorcat.irc.event.recv.IRCRecvUserMode;
import com.vectorcat.irc.event.recv.IRCRecvVersion;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeDeOp;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeDeVoice;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeOp;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveChannelBan;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveChannelKey;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveChannelLimit;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveInviteOnly;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveModerated;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveNoExternalMessages;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemovePrivate;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveSecret;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeRemoveTopicProtection;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetChannelBan;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetChannelKey;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetChannelLimit;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetInviteOnly;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetModerated;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetNoExternalMessages;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetPrivate;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetSecret;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeSetTopicProtection;
import com.vectorcat.irc.event.recv.mode.IRCRecvModeVoice;
import com.vectorcat.irc.event.send.IRCSendRaw;
import com.vectorcat.irc.exception.IRCBadServerResponse;
import com.vectorcat.irc.util.EventMonitor;
import com.vectorcat.irc.util.EventVisitor;
import com.vectorcat.irc.util.NetworkHandler;
import com.vectorcat.irc.util.WhyDoINeedThisReader;

@Singleton
public final class IRCProtocol extends AbstractExecutionThreadService {
	private class Subscriber {
		@Subscribe
		public void onRecvFinger(IRCRecvFinger event) {
			postRawMessage("NOTICE " + event.getNickname() + " :\u0001FINGER "
					+ MY_FINGER + "\u0001");
		}

		@Subscribe
		public void onRecvMessage(IRCRecvMessage event) {
			String lowerPrefixMatch = getMyNickname().toLowerCase() + " ";
			boolean nicknameMessage = event instanceof IRCRecvNicknameMessage;
			String message = event.getMessage();
			if (nicknameMessage
					|| message.toLowerCase().startsWith(lowerPrefixMatch)) {
				String command = nicknameMessage ? message : message
						.substring(lowerPrefixMatch.length());
				command = command.trim();
				bus.post(new IRCRecvCommandMessage(event.getTarget(), event
						.getNickname(), event.getLogin(), event.getHostname(),
						event.getMessage(), command));
			}
		}

		@Subscribe
		public void onRecvMode(IRCRecvMode event) {
			String target = event.getTarget();
			String hostname = event.getHostname();
			String login = event.getLogin();
			String mode = event.getMode();
			String nickname = event.getNickname();

			if (CHANNEL_PREFIXES.indexOf(target.charAt(0)) >= 0) {
				// The mode of a channel is being changed.
				String channel = target;
				StringTokenizer tok = new StringTokenizer(mode);
				String[] params = new String[tok.countTokens()];

				int t = 0;
				while (tok.hasMoreTokens()) {
					params[t] = tok.nextToken();
					t++;
				}

				char pn = ' ';
				int p = 1;

				// All of this is very large and ugly, but it's the only way of
				// providing
				// what the users want :-/
				for (int i = 0; i < params[0].length(); i++) {
					char atPos = params[0].charAt(i);

					if (atPos == '+' || atPos == '-') {
						pn = atPos;
					} else {
						String param = params[p];
						if (atPos == 'o') {
							if (pn == '+') {
								bus.post(new IRCRecvModeOp(channel, nickname,
										login, hostname, param));
							} else {
								bus.post(new IRCRecvModeDeOp(channel, nickname,
										login, hostname, param));
							}
							p++;
						} else if (atPos == 'v') {
							if (pn == '+') {
								bus.post(new IRCRecvModeVoice(channel,
										nickname, login, hostname, param));
							} else {
								bus.post(new IRCRecvModeDeVoice(channel,
										nickname, login, hostname, param));
							}
							p++;
						} else if (atPos == 'k') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelKey(channel,
										nickname, login, hostname, param));
							} else {
								bus.post(new IRCRecvModeRemoveChannelKey(
										channel, nickname, login, hostname,
										param));
							}
							p++;
						} else if (atPos == 'l') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelLimit(
										channel, nickname, login, hostname,
										Integer.parseInt(param)));
								p++;
							} else {
								bus.post(new IRCRecvModeRemoveChannelLimit(
										channel, nickname, login, hostname));
							}
						} else if (atPos == 'b') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelBan(channel,
										nickname, login, hostname, param));
							} else {
								bus.post(new IRCRecvModeRemoveChannelBan(
										channel, nickname, login, hostname,
										param));
							}
							p++;
						} else if (atPos == 't') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetTopicProtection(
										channel, nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveTopicProtection(
										channel, nickname, login, hostname));
							}
						} else if (atPos == 'n') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetNoExternalMessages(
										channel, nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveNoExternalMessages(
										channel, nickname, login, hostname));
							}
						} else if (atPos == 'i') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetInviteOnly(channel,
										nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveInviteOnly(
										channel, nickname, login, hostname));
							}
						} else if (atPos == 'm') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetModerated(channel,
										nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveModerated(
										channel, nickname, login, hostname));
							}
						} else if (atPos == 'p') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetPrivate(channel,
										nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemovePrivate(channel,
										nickname, login, hostname));
							}
						} else if (atPos == 's') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetSecret(channel,
										nickname, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveSecret(channel,
										nickname, login, hostname));
							}
						}
					}
				}
			} else {
				// The mode of a user is being changed.
				String user = target;
				bus.post(new IRCRecvUserMode(user, nickname, login, hostname,
						mode));
			}
		}

		@Subscribe
		public void onRecvNickChange(IRCRecvNickChange event) {
			if (event.getNickname().equals(getMyNickname())) {
				myCurrentName = event.getNewNickname();
			}
			bus.post(new IRCRecvMyNickChanged(event.getNickname(),
					myCurrentName));
		}

		@Subscribe
		public void onRecvPing(IRCRecvPing event) {
			postRawMessage("NOTICE " + event.getNickname() + " :\u0001PING "
					+ event.getMessage() + "\u0001");
		}

		@Subscribe
		public void onRecvRaw(IRCRecvRaw event) {
			String line = event.getMessage();
			if (line.startsWith("PING ")) {
				bus.post(new IRCRecvServerPing(line.substring(5)));
				return;
			}

			String nickname = "";
			String login = "";
			String hostname = "";

			StringTokenizer tokenizer = new StringTokenizer(line);
			String senderInfo = tokenizer.nextToken();
			String command = tokenizer.nextToken();
			String target = null;

			int exclamation = senderInfo.indexOf("!");
			int at = senderInfo.indexOf("@");
			if (senderInfo.startsWith(":")) {
				if (exclamation > 0 && at > 0 && exclamation < at) {
					nickname = senderInfo.substring(1, exclamation);
					login = senderInfo.substring(exclamation + 1, at);
					hostname = senderInfo.substring(at + 1);
				} else {

					if (tokenizer.hasMoreTokens()) {
						String token = command;

						int code = -1;
						try {
							code = Integer.parseInt(token);
						} catch (NumberFormatException e) {
							// Keep the existing value.
						}

						if (code != -1) {
							String errorStr = token;
							String response = line
									.substring(
											line.indexOf(errorStr,
													senderInfo.length()) + 4,
											line.length());
							bus.post(new IRCRecvServerResponse(code, response));
							// Return from the method.
							return;
						} else {
							// This is not a server response.
							// It must be a nick without login and hostname.
							// (or maybe a NOTICE or suchlike from the server)
							nickname = senderInfo;
							target = token;
						}
					} else {
						bus.post(new IRCRecvUnknown(line));
						return;
					}

				}
			}

			command = command.toUpperCase();
			if (nickname.startsWith(":")) {
				nickname = nickname.substring(1);
			}
			if (target == null) {
				target = tokenizer.nextToken();
			}
			if (target.startsWith(":")) {
				target = target.substring(1);
			}

			// Check for CTCP requests.
			if (command.equals("PRIVMSG") && line.indexOf(":\u0001") > 0
					&& line.endsWith("\u0001")) {
				String request = line.substring(line.indexOf(":\u0001") + 2,
						line.length() - 1);
				if (request.equals("VERSION")) {
					bus.post(new IRCRecvVersion(nickname, login, hostname,
							target));
				} else if (request.startsWith("ACTION ")) {
					bus.post(new IRCRecvAction(nickname, login, hostname,
							target, request.substring(7)));
				} else if (request.startsWith("PING ")) {
					bus.post(new IRCRecvPing(nickname, login, hostname, target,
							request.substring(5)));
				} else if (request.equals("TIME")) {
					bus.post(new IRCRecvTime(nickname, login, hostname, target));
				} else if (request.equals("FINGER")) {
					bus.post(new IRCRecvFinger(nickname, login, hostname,
							target));
				} else if ((tokenizer = new StringTokenizer(request))
						.countTokens() >= 5
						&& tokenizer.nextToken().equals("DCC")) {
					bus.post(new IRCRecvDCC(nickname, login, hostname, request));
				} else {
					bus.post(new IRCRecvUnknown(line));
				}
			} else if (command.equals("PRIVMSG")
					&& CHANNEL_PREFIXES.indexOf(target.charAt(0)) >= 0) {
				bus.post(new IRCRecvChannelMessage(target, nickname, login,
						hostname, line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("PRIVMSG")) {
				bus.post(new IRCRecvNicknameMessage(nickname, login, hostname,
						line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("JOIN")) {
				String channel = target;
				bus.post(new IRCRecvJoin(channel, nickname, login, hostname));
			} else if (command.equals("PART")) {
				String channel = target;
				bus.post(new IRCRecvPart(channel, nickname, login, hostname));
			} else if (command.equals("NICK")) {
				String newNick = target;
				bus.post(new IRCRecvNickChange(nickname, login, hostname,
						newNick));
			} else if (command.equals("NOTICE")) {
				bus.post(new IRCRecvNotice(nickname, login, hostname, target,
						line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("QUIT")) {
				bus.post(new IRCRecvQuit(nickname, login, hostname, line
						.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("KICK")) {
				String recipient = tokenizer.nextToken();
				bus.post(new IRCRecvKick(target, nickname, login, hostname,
						recipient, line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("MODE")) {
				String mode = line.substring(line.indexOf(target, 2)
						+ target.length() + 1);
				if (mode.startsWith(":")) {
					mode = mode.substring(1);
				}
				bus.post(new IRCRecvMode(target, nickname, login, hostname,
						mode));
			} else if (command.equals("TOPIC")) {
				bus.post(new IRCRecvTopic(target, line.substring(line
						.indexOf(" :") + 2), nickname, System
						.currentTimeMillis(), true));
			} else if (command.equals("INVITE")) {
				bus.post(new IRCRecvInvite(target, nickname, login, hostname,
						line.substring(line.indexOf(" :") + 2)));
			} else {
				bus.post(new IRCRecvUnknown(line));
			}
		}

		@Subscribe
		public void onRecvServerPing(IRCRecvServerPing event) {
			postRawMessage("PONG " + event.getResponse());
		}

		@Subscribe
		public void onRecvServerResponse(IRCRecvServerResponse event) {
			int code = event.getCode();
			String response = event.getResponse();

			if (code == Codes.RPL_LIST) {
				// This is a bit of information about a channel.
				int firstSpace = response.indexOf(' ');
				int secondSpace = response.indexOf(' ', firstSpace + 1);
				int thirdSpace = response.indexOf(' ', secondSpace + 1);
				int colon = response.indexOf(':');
				String channelName = response.substring(firstSpace + 1,
						secondSpace);
				int userCount = 0;
				try {
					userCount = Integer.parseInt(response.substring(
							secondSpace + 1, thirdSpace));
				} catch (NumberFormatException e) {
					// Stick with the value of zero.
				}
				String topic = response.substring(colon + 1);

				bus.post(new IRCRecvChannelInfo(channelName, userCount, topic));
			} else if (code == Codes.RPL_TOPIC) {
				// This is topic information about a channel we've just joined.
				int firstSpace = response.indexOf(' ');
				int secondSpace = response.indexOf(' ', firstSpace + 1);
				int colon = response.indexOf(':');
				String channelName = response.substring(firstSpace + 1,
						secondSpace);
				String topic = response.substring(colon + 1);

				bus.post(new IRCRecvTopicCode(channelName, topic));
			} else if (code == Codes.RPL_TOPICINFO) {
				StringTokenizer tokenizer = new StringTokenizer(response);
				tokenizer.nextToken();
				String channelName = tokenizer.nextToken();
				String setBy = tokenizer.nextToken();
				long date = 0;
				try {
					date = Long.parseLong(tokenizer.nextToken()) * 1000;
				} catch (NumberFormatException e) {
					// Stick with the default value of zero.
				}

				bus.post(new IRCRecvTopicInfo(channelName, setBy, date));
			} else if (code == Codes.RPL_NAMREPLY) {
				// This is a list of nicks in a channel that we've just joined.
				int channelEndIndex = response.indexOf(" :");
				String channelName = response.substring(
						response.lastIndexOf(' ', channelEndIndex - 1) + 1,
						channelEndIndex);

				ImmutableMap.Builder<String, String> builder = ImmutableMap
						.builder();

				StringTokenizer tokenizer = new StringTokenizer(
						response.substring(response.indexOf(" :") + 2));
				while (tokenizer.hasMoreTokens()) {
					String nickname = tokenizer.nextToken();
					String prefix = "";
					final String modes = "&@%+.";
					char firstChar = nickname.charAt(0);
					if (modes.indexOf(firstChar) != -1) {
						prefix = "" + firstChar;
					}
					nickname = nickname.substring(prefix.length());
					builder.put(nickname, prefix);
				}

				bus.post(new IRCRecvNameReply(channelName, builder.build()));
			} else if (code == Codes.RPL_ENDOFNAMES) {
				// This is the end of a NAMES list, so we know that we've got
				// the full list of users in the channel that we just joined.
				String channelName = response.substring(
						response.indexOf(' ') + 1, response.indexOf(" :"));
				bus.post(new IRCRecvEndOfNames(channelName));
			}
		}

		@Subscribe
		public void onRecvTime(IRCRecvTime event) {
			postRawMessage("NOTICE " + event.getNickname() + " :\u0001TIME "
					+ new Date().toString() + "\u0001");
		}

		@Subscribe
		public void onRecvVersion(IRCRecvVersion event) {
			postRawMessage("NOTICE " + event.getNickname() + " :\u0001VERSION "
					+ MY_VERSION + "\u0001");
		}

		@Subscribe
		public void onSend(IRCSendEvent event) {
			try {
				out.write(event.getRawMessage() + "\r\n");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				stop();
			}
		}

		private void postRawMessage(String rawMessage) {
			bus.post(new IRCSendRaw(rawMessage));
		}
	}

	private static final String MY_NAME = "DemodBot";
	public static final String MY_VERSION = "Demod's Horrible Bot";
	public static final String MY_FINGER = "That tickles!";

	private static final String CHANNEL_PREFIXES = "#&+!";

	private final Subscriber subscriber = new Subscriber();

	private final EventBus bus;
	private final NetworkHandler networkHandler;

	private final BufferedReader in;
	private final BufferedWriter out;

	private String myCurrentName = MY_NAME;

	@Inject
	IRCProtocol(@Named("IRC") EventBus eventBus, NetworkHandler networkHandler) {
		this.bus = eventBus;
		this.networkHandler = networkHandler;

		this.in = new BufferedReader(new WhyDoINeedThisReader(
				networkHandler.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(
				networkHandler.getOutputStream()));
	}

	public void connect(String host, int port, String nickname, String password)
			throws UnknownHostException, IOException, IRCBadServerResponse {

		try (EventMonitor<IRCRecvServerResponse> monitor = new EventMonitor<>(
				bus, IRCRecvServerResponse.class)) {

			networkHandler.connect(host, port);
			if (password != null && !password.equals("")) {
				bus.post(new IRCSendRaw("PASS " + password));
			}
			bus.post(new IRCSendRaw("NICK " + nickname));
			bus.post(new IRCSendRaw("USER " + MY_NAME + " 8 * :" + MY_VERSION));

			monitor.pollEach(new EventVisitor<IRCRecvServerResponse>() {
				@Override
				public boolean visit(IRCRecvServerResponse event)
						throws IOException {
					if (event.getCode() == Codes.RPL_MYINFO) {
						return true;// Success!
					} else if (event.getCode() == 439) {
						// Nothing, this is OK
					} else if (event.getCode() >= 400) {
						// Uh oh
						disconnect();
						throw new IRCBadServerResponse(event);
					}
					return false;
				}
			}, 5, TimeUnit.SECONDS);
		}
	}

	public void disconnect() throws IOException {
		networkHandler.disconnect();
	}

	public String getMyNickname() {
		return myCurrentName;
	}

	@Override
	protected void run() throws Exception {
		while (isRunning()) {
			String readLine = in.readLine();
			bus.post(new IRCRecvRaw(readLine));
		}
	}

	@Override
	protected void shutDown() throws Exception {
		super.shutDown();
		bus.unregister(subscriber);
	}

	@Override
	protected void startUp() throws Exception {
		super.startUp();
		bus.register(subscriber);
	}

}
