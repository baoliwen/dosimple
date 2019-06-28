package com.dosimple.common.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class DES {

	private static final SecureRandom sr = new SecureRandom();

	/** 算法名称 */
	public static final String KEY_ALGORITHM = "DESede";

	/** 算法名称/加密模式/填充方式 */
	public static final String CIPHER_ALGORITHM = "SunJCE";

	private static Key toKey(String key) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		DESedeKeySpec des = new DESedeKeySpec(Base64.decode(key));
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(des);
		return secretKey;
	}

	/**
	 * 生成密钥
	 */
	public static String createKey() {
		try {
			KeyGenerator keyGener = KeyGenerator.getInstance(KEY_ALGORITHM,
					CIPHER_ALGORITHM);
			keyGener.init(168, sr);
			Key key = keyGener.generateKey();
			return Base64.encode(key.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加密
	 */
	public static byte[] encrypt(String key, byte[] content) {
		try {
			Cipher cip = Cipher.getInstance(KEY_ALGORITHM, CIPHER_ALGORITHM);
			cip.init(Cipher.ENCRYPT_MODE, toKey(key));
			// cip.update(input);
			byte[] cipBys = cip.doFinal(content);
			return cipBys;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 */
	public static byte[] decrypt(String key, byte[] content) {
		try {
			Cipher cip = Cipher.getInstance(KEY_ALGORITHM, CIPHER_ALGORITHM);
			cip.init(Cipher.DECRYPT_MODE, toKey(key));
			// cip.update(input);
			byte[] bys = cip.doFinal(content);
			return bys;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
