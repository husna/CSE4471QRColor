
/**
 * Simple proof of concept showing off how to use the AES class.
 * NOTE: The following .jar needs to be included in the project:
 * bcprov-ext-jdk15on-147.jar
 * 
 * @author Akers
 */
public class EncryptionTest {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
    	
    	final String ALGORITHM = "PBEWithMD5AndDES";
    	
        String message = "Hello, Asma, Hitoe, Jan, and Robert!\n    I hope this"
                       + " message gets to you safely!";
	    System.out.println("Message to encrypt:");
	    System.out.println(message);
	    
        /* Info needed for key generation */
	    byte [] salt = {0,1,0xA,0xB,0xC,0xD,0xE,0xF}; // needs to be 8 bytes long
	    char[] password = {'p','a','s','s','w','o','r','d'};
	    
	    /* Symetric key encryption */
	    byte[] encryptedData = AES.encrypt(password, salt, message.getBytes(), ALGORITHM);
	    System.out.println("\nEncrypted Message:");
	    System.out.println(new String(encryptedData));
	    
	    /* Symetric key decrytpion */
	    byte[] decrytedData = AES.decrypt(password, salt, encryptedData, ALGORITHM);
	    System.out.println("\nDecrypted Message:");
	    System.out.println(new String(decrytedData));
	}
}	
