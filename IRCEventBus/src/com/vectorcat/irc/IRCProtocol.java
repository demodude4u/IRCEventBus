package com.vectorcat.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vectorcat.irc.event.IRCSendEvent;
import com.vectorcat.irc.event.IRCServerConnect;
import com.vectorcat.irc.event.recv.IRCRecvAction;
import com.vectorcat.irc.event.recv.IRCRecvChannelInfo;
import com.vectorcat.irc.event.recv.IRCRecvChannelMessage;
import com.vectorcat.irc.event.recv.IRCRecvCommand;
import com.vectorcat.irc.event.recv.IRCRecvDCC;
import com.vectorcat.irc.event.recv.IRCRecvDirectedMessage;
import com.vectorcat.irc.event.recv.IRCRecvEndOfNames;
import com.vectorcat.irc.event.recv.IRCRecvError;
import com.vectorcat.irc.event.recv.IRCRecvFinger;
import com.vectorcat.irc.event.recv.IRCRecvInvite;
import com.vectorcat.irc.event.recv.IRCRecvJoin;
import com.vectorcat.irc.event.recv.IRCRecvKick;
import com.vectorcat.irc.event.recv.IRCRecvMessage;
import com.vectorcat.irc.event.recv.IRCRecvMode;
import com.vectorcat.irc.event.recv.IRCRecvNameReply;
import com.vectorcat.irc.event.recv.IRCRecvNickChange;
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
import com.vectorcat.irc.event.recv.IRCRecvUserMessage;
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
import com.vectorcat.irc.event.send.IRCSendMessage;
import com.vectorcat.irc.event.send.IRCSendRaw;
import com.vectorcat.irc.exception.IRCBadServerResponse;
import com.vectorcat.irc.util.Arguments;
import com.vectorcat.irc.util.EventMonitor;
import com.vectorcat.irc.util.EventVisitor;
import com.vectorcat.irc.util.NetworkHandler;
import com.vectorcat.irc.util.WhyDoINeedThisReader;

@Singleton
class IRCProtocol extends AbstractExecutionThreadService {
	private class Subscriber {
		private void checkAndProcessCommand(boolean directedAtMe,
				Target target, User user, String login, String hostname,
				String rawMessage, String message) {
			boolean isCommand = message.charAt(0) == '!';
			if (isCommand) {
				String[] split = message.split(" ", 2);
				String command = split[0].substring(1).toUpperCase();
				Arguments arguments = new Arguments(split.length > 1 ? split[1]
						: "");

				bus.post(new IRCRecvCommand(target, user, login, hostname,
						rawMessage, message, directedAtMe, command, arguments));
			}
		}

		@Subscribe
		public void onRecvDirectedMessage(IRCRecvDirectedMessage event) {
			checkAndProcessCommand(true, event.getTarget(), event.getUser(),
					event.getLogin(), event.getHostname(),
					event.getRawMessage(), event.getMessage());
		}

		@Subscribe
		public void onRecvFinger(IRCRecvFinger event) {
			postRawMessage("NOTICE " + event.getUser() + " :\u0001FINGER "
					+ MY_FINGER + "\u0001");
		}

		@Subscribe
		public void onRecvMessage(IRCRecvMessage event) {
			if (ignoredTargets.contains(event.getUser())) {
				return;
			}

			boolean isDirected = event instanceof IRCRecvUserMessage;
			String message = event.getMessage().trim();
			String[] split = message.split("[:,]");
			if (split.length > 0 && state.getMyUser().equalsString(split[0])) {
				isDirected = true;
				message = message.substring(split[0].length() + 1);
			}
			message = message.trim();
			if (isDirected) {
				bus.post(new IRCRecvDirectedMessage(event.getTarget(), event
						.getUser(), event.getLogin(), event.getHostname(),
						event.getMessage(), message));
			} else {
				checkAndProcessCommand(false, event.getTarget(),
						event.getUser(), event.getLogin(), event.getHostname(),
						event.getMessage(), message);
			}
		}

		@Subscribe
		public void onRecvMode(IRCRecvMode event) {
			Target target = event.getTarget();
			String hostname = event.getHostname();
			String login = event.getLogin();
			String mode = event.getMode();
			User user = event.getUser();

			if (target.isChannel()) {
				// The mode of a channel is being changed.
				Channel channel = target.asChannel();
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
						String param = (p >= params.length) ? null : params[p];
						if (atPos == 'o') {
							if (pn == '+') {
								bus.post(new IRCRecvModeOp(channel, user,
										login, hostname, handles.getUser(param)));
							} else {
								bus.post(new IRCRecvModeDeOp(channel, user,
										login, hostname, handles.getUser(param)));
							}
							p++;
						} else if (atPos == 'v') {
							if (pn == '+') {
								bus.post(new IRCRecvModeVoice(channel, user,
										login, hostname, param));
							} else {
								bus.post(new IRCRecvModeDeVoice(channel, user,
										login, hostname, handles.getUser(param)));
							}
							p++;
						} else if (atPos == 'k') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelKey(channel,
										user, login, hostname, param));
							} else {
								bus.post(new IRCRecvModeRemoveChannelKey(
										channel, user, login, hostname, param));
							}
							p++;
						} else if (atPos == 'l') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelLimit(
										channel, user, login, hostname, Integer
												.parseInt(param)));
								p++;
							} else {
								bus.post(new IRCRecvModeRemoveChannelLimit(
										channel, user, login, hostname));
							}
						} else if (atPos == 'b') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetChannelBan(channel,
										user, login, hostname, param));
							} else {
								bus.post(new IRCRecvModeRemoveChannelBan(
										channel, user, login, hostname, param));
							}
							p++;
						} else if (atPos == 't') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetTopicProtection(
										channel, user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveTopicProtection(
										channel, user, login, hostname));
							}
						} else if (atPos == 'n') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetNoExternalMessages(
										channel, user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveNoExternalMessages(
										channel, user, login, hostname));
							}
						} else if (atPos == 'i') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetInviteOnly(channel,
										user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveInviteOnly(
										channel, user, login, hostname));
							}
						} else if (atPos == 'm') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetModerated(channel,
										user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveModerated(
										channel, user, login, hostname));
							}
						} else if (atPos == 'p') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetPrivate(channel,
										user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemovePrivate(channel,
										user, login, hostname));
							}
						} else if (atPos == 's') {
							if (pn == '+') {
								bus.post(new IRCRecvModeSetSecret(channel,
										user, login, hostname));
							} else {
								bus.post(new IRCRecvModeRemoveSecret(channel,
										user, login, hostname));
							}
						}
					}
				}
			} else {
				// The mode of a user is being changed.
				User recipient = target.asUser();
				bus.post(new IRCRecvUserMode(recipient, user, login, hostname,
						mode));
			}
		}

		@Subscribe
		public void onRecvPing(IRCRecvPing event) {
			postRawMessage("NOTICE " + event.getUser() + " :\u0001PING "
					+ event.getMessage() + "\u0001");
		}

		@Subscribe
		public void onRecvRaw(IRCRecvRaw event) {
			String line = event.getMessage();
			if (line.startsWith("PING ")) {
				bus.post(new IRCRecvServerPing(line.substring(5)));
				return;
			}
			if (line.startsWith("ERROR ")) {
				bus.post(new IRCRecvError(line.substring(6)));
				return;
			}

			String nickname = "";
			String login = "";
			String hostname = "";

			StringTokenizer tokenizer = new StringTokenizer(line);
			String senderInfo = tokenizer.nextToken();
			String command = tokenizer.nextToken();
			String targetString = null;

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
							targetString = token;
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
			User user = handles.getUser(nickname);

			if (targetString == null) {
				targetString = tokenizer.nextToken();
			}
			if (targetString.startsWith(":")) {
				targetString = targetString.substring(1);
			}
			Target target = getTarget(targetString);

			// Check for CTCP requests.
			if (command.equals("PRIVMSG") && line.indexOf(":\u0001") > 0
					&& line.endsWith("\u0001")) {
				String request = line.substring(line.indexOf(":\u0001") + 2,
						line.length() - 1);
				if (request.equals("VERSION")) {
					bus.post(new IRCRecvVersion(user, login, hostname, target));
				} else if (request.startsWith("ACTION ")) {
					bus.post(new IRCRecvAction(user, login, hostname, target,
							request.substring(7)));
				} else if (request.startsWith("PING ")) {
					bus.post(new IRCRecvPing(user, login, hostname, target,
							request.substring(5)));
				} else if (request.equals("TIME")) {
					bus.post(new IRCRecvTime(user, login, hostname, target));
				} else if (request.equals("FINGER")) {
					bus.post(new IRCRecvFinger(user, login, hostname, target));
				} else if ((tokenizer = new StringTokenizer(request))
						.countTokens() >= 5
						&& tokenizer.nextToken().equals("DCC")) {
					bus.post(new IRCRecvDCC(user, login, hostname, request));
				} else {
					bus.post(new IRCRecvUnknown(line));
				}
			} else if (command.equals("PRIVMSG") && target.isChannel()) {
				bus.post(new IRCRecvChannelMessage(target.asChannel(), user,
						login, hostname, line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("PRIVMSG")) {
				bus.post(new IRCRecvUserMessage(user, login, hostname, line
						.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("JOIN")) {
				Channel channel = target.asChannel();
				bus.post(new IRCRecvJoin(channel, user, login, hostname));
			} else if (command.equals("PART")) {
				Channel channel = target.asChannel();
				bus.post(new IRCRecvPart(channel, user, login, hostname));
			} else if (command.equals("NICK")) {
				User newUser = target.asUser();
				bus.post(new IRCRecvNickChange(user, login, hostname, newUser));
			} else if (command.equals("NOTICE")) {
				bus.post(new IRCRecvNotice(user, login, hostname, target, line
						.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("QUIT")) {
				bus.post(new IRCRecvQuit(user, login, hostname, line
						.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("KICK")) {
				String recipient = tokenizer.nextToken();
				bus.post(new IRCRecvKick(target.asChannel(), user, login,
						hostname, recipient,
						line.substring(line.indexOf(" :") + 2)));
			} else if (command.equals("MODE")) {
				String mode = line.substring(line.indexOf(targetString, 2)
						+ targetString.length() + 1);
				if (mode.startsWith(":")) {
					mode = mode.substring(1);
				}
				bus.post(new IRCRecvMode(target, user, login, hostname, mode));
			} else if (command.equals("TOPIC")) {
				bus.post(new IRCRecvTopic(target.asChannel(), line
						.substring(line.indexOf(" :") + 2), user, System
						.currentTimeMillis(), true));
			} else if (command.equals("INVITE")) {
				bus.post(new IRCRecvInvite(target, user, login, hostname, line
						.substring(line.indexOf(" :") + 2)));
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
				Channel channel = handles.getChannel(response.substring(
						firstSpace + 1, secondSpace));
				int userCount = 0;
				try {
					userCount = Integer.parseInt(response.substring(
							secondSpace + 1, thirdSpace));
				} catch (NumberFormatException e) {
					// Stick with the value of zero.
				}
				String topic = response.substring(colon + 1);

				bus.post(new IRCRecvChannelInfo(channel, userCount, topic));
			} else if (code == Codes.RPL_TOPIC) {
				// This is topic information about a channel we've just joined.
				int firstSpace = response.indexOf(' ');
				int secondSpace = response.indexOf(' ', firstSpace + 1);
				int colon = response.indexOf(':');
				Channel channel = handles.getChannel(response.substring(
						firstSpace + 1, secondSpace));
				String topic = response.substring(colon + 1);

				bus.post(new IRCRecvTopicCode(channel, topic));
			} else if (code == Codes.RPL_TOPICINFO) {
				StringTokenizer tokenizer = new StringTokenizer(response);
				tokenizer.nextToken();
				Channel channel = handles.getChannel(tokenizer.nextToken());
				User setBy = handles.getUser(tokenizer.nextToken());
				long date = 0;
				try {
					date = Long.parseLong(tokenizer.nextToken()) * 1000;
				} catch (NumberFormatException e) {
					// Stick with the default value of zero.
				}

				bus.post(new IRCRecvTopicInfo(channel, setBy, date));
			} else if (code == Codes.RPL_NAMREPLY) {
				// This is a list of nicks in a channel that we've just joined.
				int channelEndIndex = response.indexOf(" :");
				Channel channel = handles.getChannel(response.substring(
						response.lastIndexOf(' ', channelEndIndex - 1) + 1,
						channelEndIndex));

				ImmutableMap.Builder<User, String> builder = ImmutableMap
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
					builder.put(handles.getUser(nickname), prefix);
				}

				bus.post(new IRCRecvNameReply(channel, builder.build()));
			} else if (code == Codes.RPL_ENDOFNAMES) {
				// This is the end of a NAMES list, so we know that we've got
				// the full list of users in the channel that we just joined.
				Channel channel = handles.getChannel(response.substring(
						response.indexOf(' ') + 1, response.indexOf(" :")));
				bus.post(new IRCRecvEndOfNames(channel));
			}
		}

		@Subscribe
		public void onRecvTime(IRCRecvTime event) {
			postRawMessage("NOTICE " + event.getUser() + " :\u0001TIME "
					+ new Date() + "\u0001");
		}

		@Subscribe
		public void onRecvVersion(IRCRecvVersion event) {
			postRawMessage("NOTICE " + event.getUser() + " :\u0001VERSION "
					+ MY_VERSION + "\u0001");
		}

		@Subscribe
		public void onSend(IRCSendEvent event) {
			if (event instanceof IRCSendMessage && mute) {
				return;
			}
			try {
				out.write(event.getRawMessage() + "\r\n");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

		private void postRawMessage(String rawMessage) {
			bus.post(new IRCSendRaw(rawMessage));
		}
	}

	public static final String MY_VERSION = "Demod's Horrible Bot";
	public static final String MY_FINGER = "That tickles!";

	private final Subscriber subscriber = new Subscriber();

	private final EventBus bus;
	private final IRCHandles handles;
	private final IRCState state;
	private final NetworkHandler networkHandler;

	private final BufferedReader in;
	private final BufferedWriter out;

	private final Set<Target> ignoredTargets = Sets.newHashSet();

	private Optional<Server> server = Optional.absent();
	private boolean mute = false;

	@Inject
	IRCProtocol(EventBus eventBus, IRCHandles handles, IRCState state,
			NetworkHandler networkHandler) {
		this.bus = eventBus;
		this.handles = handles;
		this.state = state;
		this.networkHandler = networkHandler;

		this.in = new BufferedReader(new WhyDoINeedThisReader(
				networkHandler.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(
				networkHandler.getOutputStream()));

	}

	public void addIgnore(Target target) {
		ignoredTargets.add(target);
	}

	public void connect(Server server) throws UnknownHostException,
			IOException, IRCBadServerResponse {

		try (EventMonitor<IRCRecvServerResponse> monitor = new EventMonitor<>(
				bus, IRCRecvServerResponse.class)) {

			networkHandler.connect(server.getHost(), server.getPort());
			String password = server.getPassword();
			if (password != null && !password.equals("")) {
				bus.post(new IRCSendRaw("PASS " + password));
			}
			User myUser = server.getUser();
			bus.post(new IRCSendRaw("NICK " + myUser));
			bus.post(new IRCSendRaw("USER " + myUser + " 8 * :" + MY_VERSION));

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
			}, 30, TimeUnit.SECONDS);

			this.server = Optional.of(server);

			bus.post(new IRCServerConnect(server));
		}
	}

	public void disconnect() throws IOException {
		server = Optional.absent();
		networkHandler.disconnect();
	}

	public Optional<Server> getServer() {
		return server;
	}

	private Target getTarget(String target) {
		if (Target.isChannel(target)) {
			return handles.getChannel(target);
		} else {
			return handles.getUser(target);
		}
	}

	public boolean isConnected() {
		return networkHandler.isConnected();
	}

	public void mute() {
		mute = true;
	}

	@Override
	protected void run() throws Exception {
		while (isRunning()) {
			try {
				String readLine = in.readLine();
				bus.post(new IRCRecvRaw(readLine));
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void unmute() {
		mute = false;
	}

}
