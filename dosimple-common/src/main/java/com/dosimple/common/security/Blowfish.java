package com.dosimple.common.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Blowfish {

	public static byte[] encrypt(byte[] KeyData, byte[] data) {
		try {
			// create a key generator based upon the Blowfish cipher
			SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, KS);
			// encrypt message
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] KeyData, byte[] data) {
		try {
			// create a key generator based upon the Blowfish cipher
			SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, KS);
			// encrypt message
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
