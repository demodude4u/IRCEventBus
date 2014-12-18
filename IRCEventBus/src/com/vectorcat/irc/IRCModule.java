package com.vectorcat.irc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class IRCModule extends AbstractModule {

	@Override
	protected void configure() {
		final EventBus bus = new EventBus();
		final EventBus recvBus = new EventBus();

		bind(EventBus.class).toInstance(bus);
		bind(EventBus.class).annotatedWith(Names.named("recvBus")).toInstance(
				recvBus);

		recvBus.register(new Object() {
			@Subscribe
			public void onAnything(Object event) {
				bus.post(event);
			}
		});

		install(new FactoryModuleBuilder().build(IRCHandles.class));
	}

}
