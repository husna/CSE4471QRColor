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
import android.util.Log;

public class Crypto {

	public static String encrypt(byte[] salt, String cleartext) {
		byte[] rawKey = null;
		byte[] result = null;

		rawKey = getRawKey(salt);
		result = encrypt(rawKey, cleartext.getBytes());

		return toHex(result);
		// return new String(result); // this works too ...
	}

	public static String decrypt(byte[] salt, String encrypted) {
		byte[] rawKey = null;
		byte[] result = null;

		rawKey = getRawKey(salt);
		byte[] enc = toByte(encrypted);
		result = decrypt(rawKey, enc);

		return new String(result);
		// TODO test non-hex encoding 
		// return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) {
		KeyGenerator kgen;
		byte[] raw = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(seed);
			kgen.init(128, sr); // 192 and 256 bits may not be available
			SecretKey skey = kgen.generateKey();
			raw = skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException",
					"NoSuchAlgorithmException Crypto.getRawKey");
			e.printStackTrace();
		}

		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) {// throws Exception
																// {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		byte[] encrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encrypted = cipher.doFinal(clear);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher;
		byte[] decrypted = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			decrypted = cipher.doFinal(encrypted);
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoSuchAlgorithmException",
					"NoSuchAlgorithmException private decrypt");
			decrypted = encrypted;
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Log.d("NoSuchPaddingException",
					"NoSuchPaddingException private decrypt");
			decrypted = encrypted;
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Log.d("InvalidKeyException", "InvalidKeyException private decrypt");
			decrypted = encrypted;
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Log.d("IllegalBlockSizeException",
					"IllegalBlockSizeException private decrypt");
			decrypted = encrypted;
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Log.d("BadPaddingException", "BadPaddingException private decrypt");
			decrypted = encrypted;
			e.printStackTrace();
		}

		// TODO remove log.debugging statements before release
		Log.d("SIMPLE_CRYPTO_DECRYPT", new String(decrypted));

		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

}
