package edu.osu.cse4471.zxingpoc;

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
import android.widget.TextView;

public class MainActivity extends Activity {

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

		// TODO handle null editText exception
		
		// TODO handle null password exception
		
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
			// acquire encrypted text from QR Code
			String encryptedText = scanResult.getContents();
			
			// TODO handle null password field 

			// acquire user password
			EditText passText = (EditText)findViewById(R.id.password);

			// TODO prompt users for response colors
			
			// generate salt values for symetric key
			byte[] salt = AES.saltShaker(passText.getText().toString(),
					Color.BLACK, Color.RED, Color.GREEN);

			// acquire decrypted plain text
			String decryptedData = Crypto.decrypt(salt, encryptedText);

			// Create the text view
			TextView textView = new TextView(this);
			textView.setTextSize(40);
			textView.setText(decryptedData);

			// Set the text view as the activity layout
			setContentView(textView);
		}
		// else continue with any other code you need in the method
	}
}
