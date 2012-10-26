package edu.osu.cse4471.encryption;

import android.graphics.Color;
import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
	public static byte[] saltShaker(String password, int c1, int c2, int c3) {
		byte[] salt = new byte[BYTE];
		
		/* high 4 bits */
		salt[0] = (byte)(Color.red(c1) + Color.red(c2) + Color.red(c3));
		salt[1] = (byte)(Color.blue(c1) + Color.blue(c2) + Color.blue(c3));
		salt[2] = (byte)(Color.green(c1) + Color.green(c2) + Color.green(c3));
		salt[3] = (byte)Color.red(c3);
		
		/* low 4 bits */
		salt[4] = (byte)password.getBytes()[password.length()-1];
		salt[5] = (byte)Color.green(c1);
		salt[6] = (byte)Color.green(c2);
		salt[7] = (byte)( Color.red(c2) + Color.green(c1) + Color.blue(c3));
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
			Cipher dcipher = Cipher.getInstance(algorithm);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			
			decryptedData = dcipher.update(encryptedData, 0, 1);
			
			
			
			/* Decrypt */
			//decryptedData = dcipher.doFinal(encryptedData);
		}  catch (InvalidKeyException ex) {
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
//		}
		 finally{}
		
		return decryptedData;
	}

	
	//public static byte[] encrypt2(byte[] salt, byte[] clear) throws Exception {
		public static byte[] encrypt2(byte[] raw, byte[] encrypted) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
                return decrypted;
        }
	
	public static byte[] decrypt2(byte[] salt, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(salt, "AES");
            Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
    }
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param password a char[] representing the password used in key creation
     * @param salt a btye[] representing the salt used in key creation
     * @param plainText a byte[] representing the data to encrypt
     * @param algorithm a String representing the algorithm for ebcrypting
     * @return a byte[] represented the encrypted message.
	 */
	public static byte[] encrypt(byte[]plainText){//char[] password, byte[] salt, byte[] plainText,
								 //String algorithm) {
		byte[] encryptedData = null;
		try {
			/* Preparing the symetric key */
			//KeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS);
			//KeySpec keySpec = new PBEKeySpec(password);
			
			byte[] keyBytes = new byte[] { 0x73, 0x2f, 0x2d, 0x33, (byte) 0xc8, 0x01, 0x73, 0x2b, 0x72,
			        0x06, 0x75, 0x6c, (byte) 0xbd, 0x44, (byte) 0xf9, (byte) 0xc1, (byte) 0xc1, 0x03,
			        (byte) 0xdd, (byte) 0xd9, 0x7c, 0x7c, (byte) 0xbe, (byte) 0x8e };
			    byte[] ivBytes = new byte[] { (byte) 0xb0, 0x7b, (byte) 0xf5, 0x22, (byte) 0xc8, (byte) 0xd6,
			        0x08, (byte) 0xb8 };

			    // encrypt the data using precalculated keys

			    Cipher cEnc = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");

			    cEnc.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "DESede"), new IvParameterSpec(
			        ivBytes));
			
			
			
//			SecretKey key = SecretKeyFactory.getInstance(algorithm)
//					.generateSecret(keySpec);
//			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
//															ITERATIONS);

			/* Preparing the cipher */
//			Cipher ecipher = Cipher.getInstance(algorithm);
//			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

			/* Encrypt */
//			encryptedData = ecipher.doFinal(plainText);
			
			encryptedData = cEnc.doFinal(plainText);
			
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
		}// catch (InvalidKeySpecException ex) {
//			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
//			ex.printStackTrace();
		//}
			catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (NoSuchProviderException ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
			Log.d("ANGER", "ANGERY ME!!!");
			ex.printStackTrace();
		}
		return encryptedData;
	}
}
