import java.awt.Color;

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
    	
        String message = "Quis custodiet ipsos custodes?\nWho will guard the guards themselves?";
	    System.out.println("Message to encrypt:");
	    System.out.println(message);

	    /* Applying user's responses to the presented colors */
//	    Color[] rainbow = {Color.black, Color.BLACK, Color.blue, Color.BLUE, Color.cyan, Color.CYAN,
//	    				   Color.DARK_GRAY, Color.darkGray, Color.gray, Color.GRAY, Color.green,
//	    				   Color.GREEN, Color.LIGHT_GRAY, Color.lightGray, Color.magenta, Color.MAGENTA, 
//	                       Color.orange, Color.ORANGE, Color.pink, Color.PINK, Color.red, Color.RED, 
//	                       Color.white, Color.WHITE, Color.yellow, Color.YELLOW};
	    
	    byte[] salt = null;
	    
	    for (int i = 0; i < 256; i++) {
	    	for (int j = 0; j < 256; j++) {
	    		for (int k = 0; k < 256; k++) {
	    			Color c = new Color(i, j, k);
	    			salt = AES.saltShaker(c, c, c);
	    		}
	    	}
	    	
	    }
	    
	    
	    salt = AES.saltShaker(Color.GREEN, Color.BLUE, Color.RED);
	    
	    char[] password = {'p','a','s','s','w','o','r','d'};
	    //char[] password2 = {'p','a','s','s'};

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
