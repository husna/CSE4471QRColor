import java.awt.Color;
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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
/**
 * This class contains methods for handling AES encryption and
 * decryption.
 * @author Akers
 */
public class AES {
	private final static int ITERATIONS = 10000;
	private final static int BYTE = 8;
	
	/**
	 * TODO talk about this implementation with Jan and Robert
	 *
	 * @param c1 the first response Color 
	 * @param c2 the second response Color
	 * @param c3 the third response Color
	 * @return byte[] the salt used in generating the symetric key
	 */
	public static byte[] saltShaker(Color c1, Color c2, Color c3) {
		byte[] salt = new byte[BYTE];
		
		/* high 4 bits */
		salt[0] = (byte)(c1.getRed() + c2.getRed() + c3.getRed());
		salt[1] = (byte)(c1.getBlue() + c2.getBlue() + c3.getBlue());
		salt[2] = (byte)(c1.getGreen() + c2.getGreen() + c3.getGreen());
		salt[3] = (byte)c2.getRed();
		
		/* low 4 bits */
		salt[4] = (byte)c3.getBlue();
		salt[5] = (byte)c1.getGreen();
		salt[6] = (byte)c2.getGreen();
		salt[7] = (byte)(c2.getRed() + c1.getGreen() + c3.getBlue());
		return salt;
	}

    /**
     * @param password a char[] representing the password used in key creation
     * @param salt a byte[] representing the salt used in key creation
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
