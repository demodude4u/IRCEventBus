package com.vectorcat.irc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * The problem arises when the {@link InputStreamReader} has trouble decoding
 * the input stream. For testing purposes, this is a temporary solution that
 * assumes the bytes in the stream are directly convertible to characters.
 * Additionally, it will split the {@link #read(char[], int, int)} at every line
 * feed to help buffering.
 * 
 * @author Weston
 */
public class WhyDoINeedThisReader extends Reader {

	private final InputStream in;

	public WhyDoINeedThisReader(InputStream in) {
		this.in = in;
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int count = 0;
		while (count < len) {
			char c = cbuf[off + (count++)] = (char) in.read();
			if (c == '\n') {
				break;
			}
		}
		return count;
	}

}
