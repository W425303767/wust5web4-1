package com.wust5.util;

public final class Numbers {

	private Numbers() {
	}

	/**
	 * Reads the leading decimal digits of the given text, ignoring nothing else.
	 * Returns 0 for null or empty input.
	 */
	public static int parseDigits(String text) {
		int value = 0;
		if (null == text) {
			return value;
		}
		for (int i = 0; i < text.length(); i++) {
			value = value * 10 + (text.charAt(i) - '0');
		}
		return value;
	}
}
