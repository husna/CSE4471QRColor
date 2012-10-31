package edu.osu.cse4471.zxingpoc;

import java.util.Random;

import edu.osu.cse4471.encryption.AES;
import edu.osu.cse4471.encryption.Crypto;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	
	public final static String DISPLAY_MESSAGE = "edu.osu.cse4471.zxingpoc.MainActivity";
	public final static String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 *  This method encrypts the text that is in the 
	 *  edit_message EditText field. 
	 * @param view
	 */
	public void encryptMessage(View view) {
		// acquire password and message text fields
		EditText editText = (EditText) findViewById(R.id.edit_message);
		EditText passText = (EditText) findViewById(R.id.password);

		/* handle null editText exception */
		if (editText.getText().toString().equals("")) {
			editText.setText(generateRandomPassword(new Random(), ALPHABET, 8));
		}
		
		/* handle null password exception */
		if (passText.getText().toString().equals("")) {
			passText.setText(generateRandomPassword(new Random(), ALPHABET, 8));
		} 
		
		// generate salt values for symetric key encryption
		byte[] salt = AES.saltShaker(passText.getText().toString(),
				Color.BLACK, Color.RED, Color.GREEN);
		String eMessage = Crypto.encrypt(salt, editText.getText()
				.toString());
		
		// use this for copy/paste ecrypted QR code generator
		Log.d("ENCRYPT", eMessage);
		
		// display encrypted message
		editText.setText(eMessage);
	}

	/** Called when the user clicks the Scan QR Code button */
	public void scanQRCode(View view) {
		// Do something in response to the button being clicked.
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	/**
	 * 
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			/* acquire encrypted text from QR Code */
			String encryptedText = scanResult.getContents();
			
			/* acquire user password */
			EditText passText = (EditText)findViewById(R.id.password);
			
			/* handle null password field */
			if (passText.getText().toString().equals("")) {
				passText.setText(generateRandomPassword(new Random(), ALPHABET, 8));
			}

			/* TODO prompt users for response colors */
			
			// generate salt values for symetric key
			byte[] salt = AES.saltShaker(passText.getText().toString(),
					Color.BLACK, Color.RED, Color.GREEN);
			
			/* clears password after executing decryption */
			passText.setText("");
			
			/* acquire decrypted plain text */
			String decryptedData = Crypto.decrypt(salt, encryptedText);

			Intent dipslayMessage = new Intent(this, DisplayScannedQRCode.class);
			dipslayMessage.putExtra(DISPLAY_MESSAGE, decryptedData);
	        startActivity(dipslayMessage);
		}
	}
	
	private static String generateRandomPassword(Random rand, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rand.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
}
