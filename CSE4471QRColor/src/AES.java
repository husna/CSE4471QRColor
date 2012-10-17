import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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
import javax.crypto.spec.PBEParameterSpec;

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
	private final static int ITERATIONS = 10000;

	/**
	 * Just for test purposes. Do not actually use.
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
     * @param password a char[] representing the password used in key creation
     * @param salt a btye[] representing the salt used in key creation
     * @param encryptedData a byte[] representing the encrypted data
     * @param algorithm a String representing the algorithm for decrypting
     * @return a byte[] represented the decrypted message
     */
	public static byte[] decrypt(char[] password, byte[] salt, byte[] encryptedData,
								 String algorithm) {
		byte[] decryptedData = null;
		try {
			/*preparing the symetric key */
			KeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS);
			SecretKey key = SecretKeyFactory.getInstance(algorithm)
					.generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
					ITERATIONS);
			
			/* Prepare the cipher */
			Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			
			/* Decrypt */
			decryptedData = dcipher.doFinal(encryptedData);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (BadPaddingException ex) {
			System.out.println("Wrong key given");
			decryptedData = encryptedData;
		} catch (InvalidKeyException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (InvalidKeySpecException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		return decryptedData;
	}

	/**
	 * @param password a char[] representing the password used in key creation
     * @param salt a btye[] representing the salt used in key creation
     * @param plainText a byte[] representing the data to encrypt
     * @param algorithm a String representing the algorithm for ebcrypting
     * @return a byte[] represented the encrypted message.
	 */
	public static byte[] encrypt(char[] password, byte[] salt, byte[] plainText,
								 String algorithm) {
		byte[] encryptedData = null;
		try {
			/* Preparing the symetric key */
			KeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS);
			SecretKey key = SecretKeyFactory.getInstance(algorithm)
					.generateSecret(keySpec);
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
					ITERATIONS);
			
			/* Preparing the cipher */
			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			
			/* Encrypt */
			encryptedData = ecipher.doFinal(plainText);
		} catch (IllegalBlockSizeException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (BadPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (InvalidKeyException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (InvalidKeySpecException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
		return encryptedData;
	}
}
