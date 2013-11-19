package com.vectorcat.irc.util;

import java.io.IOException;

public interface EventVisitor<T> {
	/**
	 * @return <code>true</code> if I found what I'm looking for,
	 *         <code>false</code> to keep on visiting.
	 */
	public boolean visit(T event) throws IOException;
}
