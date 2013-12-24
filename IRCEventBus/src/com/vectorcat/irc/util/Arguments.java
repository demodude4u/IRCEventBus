package com.vectorcat.irc.util;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class Arguments implements Iterable<String> {

	private final String argumentsString;
	private final List<Point> argumentIndices = Lists.newArrayList();
	private final List<String> arguments = Lists.newArrayList();

	public Arguments(String argumentsString) {
		this.argumentsString = argumentsString;

		parseArgumentsString(argumentsString, argumentIndices, arguments);
	}

	public boolean contains(Object o) {
		return arguments.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return arguments.containsAll(c);
	}

	public String get(int index) {
		return arguments.get(index);
	}

	public int indexOf(Object o) {
		return arguments.indexOf(o);
	}

	public boolean isEmpty() {
		return arguments.isEmpty();
	}

	@Override
	public Iterator<String> iterator() {
		return arguments.iterator();
	}

	public int lastIndexOf(Object o) {
		return arguments.lastIndexOf(o);
	}

	public ListIterator<String> listIterator() {
		return arguments.listIterator();
	}

	public ListIterator<String> listIterator(int index) {
		return arguments.listIterator(index);
	}

	private void parseArgumentsString(String argumentsString,
			List<Point> argumentIndices, List<String> arguments) {

		// http://stackoverflow.com/a/3366634 (modified)
		// This matches space-delimited items, except within quotes
		final String regex = "\"([^\"]*)\"|(\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(argumentsString);
		while (matcher.find()) {
			argumentIndices.add(new Point(matcher.start(), matcher.end()));
			if (matcher.group(1) != null) {
				arguments.add(matcher.group(1));
			} else {
				arguments.add(matcher.group(2));
			}
		}
	}

	public int size() {
		return arguments.size();
	}

	public List<String> subList(int fromIndex, int toIndex) {
		return arguments.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return arguments.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return arguments.toArray(a);
	}

	@Override
	public String toString() {
		return argumentsString;
	}

	/**
	 * Return a string of trailing arguments starting at the indexed argument
	 */
	public String toString(int index) {
		return argumentsString.substring(argumentIndices.get(index).x);
	}

}
