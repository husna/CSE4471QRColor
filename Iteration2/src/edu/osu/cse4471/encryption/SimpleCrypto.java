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

	public class SimpleCrypto {

	        public static String encrypt(byte[] salt, String cleartext) {//throws Exception {
	                byte[] rawKey = null;
	                byte[] result = null;
					try {
						rawKey = getRawKey(salt);
						result = encrypt(rawKey, cleartext.getBytes());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                return toHex(result);
	        }
	        
	        public static String decrypt(byte[] salt, String encrypted) {// throws Exception {
	                byte[] rawKey = null;
	                byte[] result = null;
					try {
						rawKey = getRawKey(salt);
						byte[] enc = toByte(encrypted);
		                result = decrypt(rawKey, enc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                return new String(result);
	        }

	        private static byte[] getRawKey(byte[] seed) throws Exception {
	                KeyGenerator kgen = KeyGenerator.getInstance("AES");
	                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	                sr.setSeed(seed);
	            kgen.init(128, sr); // 192 and 256 bits may not be available
	            SecretKey skey = kgen.generateKey();
	            byte[] raw = skey.getEncoded();
	            return raw;
	        }

	        
	        private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
	            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	                Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	            byte[] encrypted = cipher.doFinal(clear);
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
						Log.d("NoSuchAlgorithmException", "HATS");
						decrypted = encrypted;
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						Log.d("NoSuchPaddingException", "HATS");
						decrypted = encrypted;
						e.printStackTrace();
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						Log.d("InvalidKeyException", "HATS");
						decrypted = encrypted;
						e.printStackTrace();
					} catch (IllegalBlockSizeException e) {
						// TODO Auto-generated catch block
						Log.d("IllegalBlockSizeException", "HATS");
						decrypted = encrypted;
						e.printStackTrace();
					} catch (BadPaddingException e) {
						// TODO Auto-generated catch block
						Log.d("BadPaddingException", "HATS");
						decrypted = encrypted;
						e.printStackTrace();
					}
	            
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
	                int len = hexString.length()/2;
	                byte[] result = new byte[len];
	                for (int i = 0; i < len; i++)
	                        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
	                return result;
	        }

	        public static String toHex(byte[] buf) {
	                if (buf == null)
	                        return "";
	                StringBuffer result = new StringBuffer(2*buf.length);
	                for (int i = 0; i < buf.length; i++) {
	                        appendHex(result, buf[i]);
	                }
	                return result.toString();
	        }
	        private final static String HEX = "0123456789ABCDEF";
	        private static void appendHex(StringBuffer sb, byte b) {
	                sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	        }
	        
	}
