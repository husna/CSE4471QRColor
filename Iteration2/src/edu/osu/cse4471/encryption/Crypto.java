package edu.osu.cse4471.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.graphics.Color;
import android.util.Log;

public class Crypto {

	// TODO document crypto class
	
	/* TODO document KEY_SIZE variable */
	private static final int KEY_SIZE = 128;
	
	/* TODO document HEX variable */
	private final static String HEX = "0123456789ABCDEF";
	
	/* TODO document BYTE variable */
	private final static int BYTE = 8;
	
	public static byte[] saltShaker(String password, int c1, int c2, int c3) {
		byte[] salt = new byte[BYTE];

		/* high 4 bits */
		salt[0] = (byte)(Color.red(c1) + Color.red(c2) + Color.red(c3));
		salt[1] = (byte)(Color.blue(c1) + Color.blue(c2) + Color.blue(c3));
		salt[2] = (byte)(Color.green(c1) + Color.green(c2) + Color.green(c3));
		salt[3] = (byte)(Color.red(c3) - password.getBytes()[0]);

		/* low 4 bits */
		salt[4] = (byte)(password.getBytes()[password.length() - 1] + 
				          password.getBytes()[0]);
		salt[5] = (byte)Color.green(c1);
		salt[6] = (byte)Color.green(c2);
		salt[7] = (byte)(Color.red(c2) + Color.green(c1) + Color.blue(c3));
		return salt;
	}
	
	
	
	/**
	 * @param salt a byte[] representing the values used to
	 * generate the block cipher.
	 * @param plainText a String representing the message that
	 * is to be encrypted.
	 * @return a String of encrypted plainText represented in
	 * hex characters.
	 */
	public static String encrypt(byte[] salt, String plainText) {
		byte[] rawKey = null;
		byte[] result = null;

		rawKey = getRawKey(salt);
		result = encrypt(rawKey, plainText.getBytes());

		return toHex(result);
	}
	
	/**
	 * @param salt a byte[] represents the values used to 
	 * generate the block cipher.
	 * @param cipherText a String representing the encrypted
	 * message.
	 * @return a String representing the original plain text.
	 */
	public static String decrypt(byte[] salt, String cipherText) {
		byte[] rawKey = null;
		byte[] result = null;

		rawKey = getRawKey(salt);
		byte[] enc = toByte(cipherText);
		
		
		
		
		result = decrypt(rawKey, enc);

		//result = decrypt(rawKey, cipherText.getBytes());
		
		return new String(result);
	}

	/**
	 * TODO document getRawKey
	 * @param seed
	 * @return
	 */
	private static byte[] getRawKey(byte[] seed) {
		KeyGenerator kgen;
		byte[] raw = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(seed);
			kgen.init(KEY_SIZE, sr);
			SecretKey skey = kgen.generateKey();
			raw = skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException",
					"NoSuchAlgorithmException Crypto.getRawKey");
			e.printStackTrace();
		}
		return raw;
	}

	/**
	 * TODO document encrypt
	 * @param raw
	 * @param plainText
	 * @return
	 */
	private static byte[] encrypt(byte[] raw, byte[] plainText) {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(plainText);
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException",
					"NoSuchAlgorithmException private decrypt");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Log.d("NoSuchPaddingException",
					"NoSuchPaddingException private decrypt");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Log.d("InvalidKeyException", "InvalidKeyException private decrypt");
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Log.d("IllegalBlockSizeException",
					"IllegalBlockSizeException private decrypt");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Log.d("BadPaddingException", "BadPaddingException private decrypt");
			e.printStackTrace();
		}
		return encrypted;
	}

	/**
	 * TODO document decrypt
	 * @param raw
	 * @param cipherText
	 * @return
	 */
	private static byte[] decrypt(byte[] raw, byte[] cipherText) {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		byte[] decrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(cipherText);
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException",
					"NoSuchAlgorithmException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Log.d("NoSuchPaddingException",
					"NoSuchPaddingException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Log.d("InvalidKeyException", "InvalidKeyException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Log.d("IllegalBlockSizeException",
					"IllegalBlockSizeException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Log.d("BadPaddingException", "BadPaddingException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		}
		// TODO remove log.debugging statements before release
		Log.d("SIMPLE_CRYPTO_DECRYPT", new String(decrypted));
		return decrypted;
	}

	/**
	 * TODO document toByte
	 * @param hexString
	 * @return
	 */
	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	/**
	 * TODO document toHex
	 * @param buf
	 * @return
	 */
	private static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			result.append(HEX.charAt((buf[i] >> 4) & 0x0f)).append(HEX.charAt(buf[i] & 0x0f));
		}
		return result.toString();
	}
}
