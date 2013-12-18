package com.vectorcat.irc;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class IRCModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventBus.class).toInstance(new EventBus());

		install(new FactoryModuleBuilder().build(IRCHandles.class));
	}

}
