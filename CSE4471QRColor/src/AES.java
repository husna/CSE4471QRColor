import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * This class contains methods for handling AES encryption and decryption. Date:
 * 10/13/2012
 * 
 * @author Akers
 */
public class AES {

	/**
	 * Byte sizes for different encryption keys
	 */
	private final static int KEY_128_BYTE = 128; // recommended for mobile
	private final static int KEY_192_BYTE = 192; // medium strength
	private final static int KEY_256_BTYE = 256; // strongest but slower

	/**
	 * @return a random symetric SecretKey to be used for encryption and
	 *         decryption.
	 */
	public static SecretKey randomSecretKey() {
		SecretKey skey = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(KEY_128_BYTE);
			skey = kgen.generateKey();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		}
		return skey;
	}

	/**
	 * TODO fix this method so that it works. Exception missing algorithm
	 * 
	 * @param password
	 *            a char[] representing the password used to make symetric key
	 * @param salt
	 *            a byte[] representing values that go into making symetric key
	 * @param algorithm
	 *            a String representing the algorithm to be used for key
	 *            generation
	 * @return a SecretKey based on the password, salt and cipher algorithm
	 */
	public static SecretKey passSecretKey(char[] password, byte[] salt,
			String algorithm) {
		PBEKeySpec mykeyspec = null;
		SecretKey key = null;
		try {
			final int HASH_ITERATIONS = 10000;
			final int KEY_LENGTH = 256;
			mykeyspec = new PBEKeySpec(password, salt, HASH_ITERATIONS,
					KEY_LENGTH);

			Set<String> algor = Security.getAlgorithms("Cipher");
			key = SecretKeyFactory.getInstance((String) (algor.toArray())[0])
					.generateSecret(mykeyspec);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeySpecException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		}
		return key;
	}

	/**
	 * @param key
	 *            a byte[] representing a symetric key for which to encrypt data
	 * @param encryptedData
	 *            a byte[] of data that is to be decrypted
	 * @return a byte[] of decrypted bytes
	 */
	public static byte[] decrypt(SecretKey key, byte[] encryptedData) {
		byte[] decryptedData = null;
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, key);
			decryptedData = c.doFinal(encryptedData);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		}
		return decryptedData;
	}

	/**
	 * A method that encrypts plain text into unreadible bytes
	 * 
	 * @param key
	 *            a byte[] representing the symetric key for encrypting the
	 *            message.
	 * @param plainText
	 *            a byte[] reprsenting the message that is to be encoded
	 * @return a byte[] representing the encrypted data.
	 */
	public static byte[] encrypt(SecretKey key, byte[] plainText) {
		byte[] encryptedData = null;
		try {
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = c.doFinal(plainText);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (BadPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidKeyException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
		}
		return encryptedData;
	}
}
