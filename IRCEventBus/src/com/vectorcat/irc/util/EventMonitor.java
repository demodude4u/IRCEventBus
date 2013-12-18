package com.vectorcat.irc.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Queues;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class EventMonitor<T> implements Closeable {

	private final EventBus bus;

	private final BlockingQueue<T> events = Queues.newLinkedBlockingDeque();

	private final Class<T> superclass;

	public EventMonitor(EventBus bus, Class<T> superclass) {
		this.bus = bus;
		this.superclass = superclass;
		bus.register(this);
	}

	@Override
	public void close() {
		bus.unregister(this);
	}

	public BlockingQueue<T> getEvents() {
		return events;
	}

	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEvent(Object event) {
		if (superclass.isAssignableFrom(event.getClass())) {
			events.add((T) event);
		}
	}

	public T poll(long timeout, TimeUnit unit) throws IOException {
		try {
			return events.poll(timeout, unit);
		} catch (InterruptedException e) {
			throw new IOException("Event Timeout!");
		}
	}

	public void pollEach(EventVisitor<T> visitor, long timeout, TimeUnit unit)
			throws IOException {
		while (true) {
			T event;
			try {
				event = getEvents().poll(timeout, unit);
			} catch (InterruptedException e) {
				throw new IOException("Interrupted!", e);
			}

			if (event == null) {
				throw new IOException("Event Timeout!");
			}

			if (visitor.visit(event)) {
				break;
			}
		}
	}

}
