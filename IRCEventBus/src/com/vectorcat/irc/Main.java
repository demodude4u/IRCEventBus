package com.vectorcat.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.vectorcat.irc.event.IRCSendEvent;
import com.vectorcat.irc.event.recv.IRCRecvRaw;
import com.vectorcat.irc.event.send.IRCSendRaw;
import com.vectorcat.irc.feature.CleverBotFeature;

public class Main {

	private static Object createEventLogger() {
		return new Object() {
			@Subscribe
			public void onAnythingElse(Object event) {
				if (event instanceof IRCRecvRaw) {
					return;
				}
				if (event instanceof IRCSendEvent) {
					return;
				}
				System.out.println(" [" + event.getClass().getSimpleName()
						+ "]");
			}

			@Subscribe
			public void onRecvRaw(IRCRecvRaw event) {
				System.out.println("RECV <- " + event.getMessage());
			}

			@Subscribe
			public void onSend(IRCSendEvent event) {
				System.out.println("##### SEND -> " + event.getRawMessage());
			}
		};
	}

	public static void main(String[] args) throws InterruptedException,
			UnknownHostException, IOException {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(EventBus.class).annotatedWith(Names.named("IRC"))
						.toInstance(new EventBus());
			}
		});

		EventBus bus = injector.getInstance(Key.get(EventBus.class,
				Names.named("IRC")));

		// Prints out send and recv, optional
		bus.register(createEventLogger());

		IRCControl control = injector.getInstance(IRCControl.class);

		// Initialize Features
		// injector.getInstance(SillyFeature.class);
		injector.getInstance(CleverBotFeature.class);

		try {

			// Initial actions
			control.connect("irc.fyrechat.net", 6667, "DemodBot", "password");
			control.join("#Vana");

			control.ignore("SolidSnake");

			// Can add more actions here, but might want to use sleep()

		} catch (Exception e) {
			e.printStackTrace();
			control.disconnect();
		}

		// Send anything typed in console as raw lines to the IRC
		BufferedReader sysin = new BufferedReader(new InputStreamReader(
				System.in));
		String line;
		while ((line = sysin.readLine()) != null) {
			bus.post(new IRCSendRaw(line));
		}

	}
}
