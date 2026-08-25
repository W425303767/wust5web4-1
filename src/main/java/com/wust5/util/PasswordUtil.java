package com.wust5.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class PasswordUtil {

	private PasswordUtil() {
	}

	public static String hash(String password) {
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return toHex(salt) + ":" + toHex(digest(salt, password));
	}

	public static boolean verify(String password, String stored) {
		if(password == null || stored == null)
			return false;
		String[] parts = stored.split(":", -1);
		if(parts.length != 2 || parts[0].length() != 32 || parts[1].length() != 64)
			return false;
		byte[] salt;
		byte[] expected;
		try {
			salt = fromHex(parts[0]);
			expected = fromHex(parts[1]);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return MessageDigest.isEqual(expected, digest(salt, password));
	}

	private static byte[] digest(byte[] salt, String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(salt);
			return digest.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (java.security.NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	private static String toHex(byte[] bytes) {
		StringBuilder result = new StringBuilder(bytes.length * 2);
		for(byte value : bytes)
			result.append(String.format("%02x", value & 0xff));
		return result.toString();
	}

	private static byte[] fromHex(String value) {
		if(value.length() % 2 != 0)
			throw new IllegalArgumentException();
		byte[] result = new byte[value.length() / 2];
		for(int i = 0; i < result.length; i++) {
			int high = Character.digit(value.charAt(i * 2), 16);
			int low = Character.digit(value.charAt(i * 2 + 1), 16);
			if(high < 0 || low < 0)
				throw new IllegalArgumentException();
			result[i] = (byte)((high << 4) + low);
		}
		return result;
	}
}
