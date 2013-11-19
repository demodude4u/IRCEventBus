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
import com.vectorcat.irc.feature.SillyFeature;

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

		bus.register(createEventLogger());

		IRCControl control = injector.getInstance(IRCControl.class);

		// Features
		injector.getInstance(SillyFeature.class);

		try {

			control.connect("irc.fyrechat.net", 6667, "DemodBot", "rye2bot");
			control.join("#DemodLand");

		} catch (Exception e) {
			e.printStackTrace();
			control.disconnect();
		}

		BufferedReader sysin = new BufferedReader(new InputStreamReader(
				System.in));
		String line;
		while ((line = sysin.readLine()) != null) {
			bus.post(new IRCSendRaw(line));
		}
	}
}