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

	/* Size of symmetric key in bits */
	private static final int KEY_SIZE = 128;
	
	/* String of all valid hex numbers */
	private final static String HEX = "0123456789ABCDEF";
	
	/* Number of bytes used in salt */
	private final static int BYTE = 8;
	
	/**
	 * @param password a String password used for generating salt values
	 * @param lowerLeft an int representing the color response to lower left color
	 * @param upperLeft an int representing the color response to upper left color
	 * @param upperRight an int representing the color response to upper right color
	 * @return a byte[] of the salt used for symmetric key generation.
	 */
	public static byte[] saltShaker(String password, int lowerLeft, int upperLeft, int upperRight) {
		byte[] salt = new byte[BYTE];

		/* high 4 bits */
		salt[0] = (byte)(Color.red(lowerLeft) + Color.red(upperLeft) + Color.red(upperRight));
		salt[1] = (byte)(Color.blue(lowerLeft) + Color.blue(upperLeft) + Color.blue(upperRight));
		salt[2] = (byte)(Color.green(lowerLeft) + Color.green(upperLeft) + Color.green(upperRight));
		salt[3] = (byte)(Color.red(upperRight) - password.getBytes()[0]);

		/* low 4 bits */
		salt[4] = (byte)(password.getBytes()[password.length() - 1] + 
				          password.getBytes()[0]);
		salt[5] = (byte)Color.green(lowerLeft);
		salt[6] = (byte)Color.green(upperLeft);
		salt[7] = (byte)(Color.red(upperLeft) + Color.green(lowerLeft) + Color.blue(upperRight));
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
		return new String(result);
	}

	/**
	 * @param salt a byte[] of salt values for symetric key generation
	 * @return a byte[]  the rawkey
	 */
	private static byte[] getRawKey(byte[] salt) {
		KeyGenerator kgen;
		byte[] raw = null;
		try {
			kgen = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(salt);
			kgen.init(KEY_SIZE, sr);
			SecretKey skey = kgen.generateKey();
			raw = skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException",
					"NoSuchAlgorithmException Crypto.getRawKey");
			e.printStackTrace();
		}
		return raw;
	}

	/**
	 * @param raw a byte[] used for symmetric key generation.
	 * @param plainText a byte[] storing the plain text message
	 * @return a byte[] of encrypted bytes
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
			Log.e("NoSuchAlgorithmException",
					"NoSuchAlgorithmException private decrypt");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Log.e("NoSuchPaddingException",
					"NoSuchPaddingException private decrypt");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Log.e("InvalidKeyException", "InvalidKeyException private decrypt");
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Log.e("IllegalBlockSizeException",
					"IllegalBlockSizeException private decrypt");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Log.e("BadPaddingException", "BadPaddingException private decrypt");
			e.printStackTrace();
		}
		return encrypted;
	}

	/**
	 * @param raw a byte[] used for symmetric key generation.
	 * @param cipherText a byte[] of the cipher text (encrypted)
	 * @return a byte[] of the plain text
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
			Log.e("NoSuchAlgorithmException",
					"NoSuchAlgorithmException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Log.e("NoSuchPaddingException",
					"NoSuchPaddingException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Log.e("InvalidKeyException", "InvalidKeyException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Log.e("IllegalBlockSizeException",
					"IllegalBlockSizeException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Log.e("BadPaddingException", "BadPaddingException private decrypt");
			decrypted = cipherText;
			e.printStackTrace();
		}
		return decrypted;
	}

	/**
	 * @param hexString a String of encrypted hex characters
	 * @return a byte[] of decrypted bytes
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
	 * @param buffer a byte[] of encrypted bytes
	 * @return a String of Hex characters
	 */
	private static String toHex(byte[] buffer) {
		if (buffer == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buffer.length);
		for (int i = 0; i < buffer.length; i++) {
			result.append(HEX.charAt((buffer[i] >> 4) & 0x0f)).append(HEX.charAt(buffer[i] & 0x0f));
		}
		return result.toString();
	}
}
