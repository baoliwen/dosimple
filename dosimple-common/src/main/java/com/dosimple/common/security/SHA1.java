package com.dosimple.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class SHA1 {

	public static String encode(byte[] content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return byteToHex(md.digest(content));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encode(String content) {
		return encode(content.getBytes());
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
