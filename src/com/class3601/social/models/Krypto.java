package com.class3601.social.models;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Krypto {

	private static final String UTF8 = "UTF8";
	private static final String DES_ENCRYPTION = "DES";
	private static final String KEYSTRING = "zyxwvutsrqponmlkjihgfedcba";

	private static KeySpec keySpec;
	private static SecretKeyFactory keyFactory;
	private static SecretKey key;
	private static Cipher cipher;
	private static Krypto defaultEncryptor;

	public Krypto() {
		byte[] bytes;
		try {

			bytes = KEYSTRING.getBytes(UTF8);
			keySpec = new DESKeySpec(bytes);
			keyFactory = SecretKeyFactory.getInstance(DES_ENCRYPTION);
			cipher = Cipher.getInstance(DES_ENCRYPTION);
			key = keyFactory.generateSecret(keySpec);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		setDefaultEncryptor(this);
	}

	public String encrypt(String bytes) {

		String result = null;

		try {

			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] b = cipher.doFinal(bytes.getBytes(UTF8));
			result = new String(Base64.encodeBase64(b));

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return result;
	}

	public String decrypt(String bytes) {

		String result = null;

		try {

			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encoded = Base64.decodeBase64(bytes);
			byte[] b = cipher.doFinal(encoded);
			result = new String(b);

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Krypto getDefaultEncryptor() {
		return defaultEncryptor;
	}

	public static void setDefaultEncryptor(Krypto defaultEncryptor) {
		Krypto.defaultEncryptor = defaultEncryptor;
	}
}
