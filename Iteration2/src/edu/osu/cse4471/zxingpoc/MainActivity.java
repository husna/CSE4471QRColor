package edu.osu.cse4471.zxingpoc;

import edu.osu.cse4471.encryption.Crypto;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	/* ID for activity passing from MainActivity */
	public final static String DISPLAY_MESSAGE = "edu.osu.cse4471.zxingpoc.MainActivity";

	/* a String of all the valid letters used in random password generation */
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
	 * This method encrypts the text that is in the edit_message EditText field.
	 * 
	 * @param view
	 *            the current View object
	 */
	public void encryptMessage(View view) {
		/* acquire password and plain text fields */
		EditText plainText = (EditText) findViewById(R.id.edit_message);
		EditText password = (EditText) findViewById(R.id.password);

		/* handle null plain text exception */
		if (plainText.getText().toString().equals("")) {
			plainText.setText(generateRandomPassword(ALPHABET, 8));
		}

		/* handle null password exception */
		if (password.getText().toString().equals("")) {
			password.setText(generateRandomPassword(ALPHABET, 8));
		}

		/* generate salt values for symmetric key encryption */
		byte[] salt = Crypto.saltShaker(password.getText().toString(),
				Color.BLUE, Color.RED, Color.GREEN);

		/* generate cipher text */
		String cipherText = Crypto
				.encrypt(salt, plainText.getText().toString());

		/* clears password after encrypting */
		password.setText("");

		/*
		 * TODO [DELETEME before due] use this for copy/paste ecrypted QR code
		 * generator
		 */
		Log.d("ENCRYPT", cipherText);

		/* launches DisplayScannedQRCode activity to display cipher text */
		Intent dipslayMessage = new Intent(this, DisplayScannedQRCode.class);
		dipslayMessage.putExtra(DISPLAY_MESSAGE, cipherText);
		startActivity(dipslayMessage);
	}

	/* Called when the user clicks the Scan QR Code button */
	public void scanQRCode(View view) {
		// Do something in response to the button being clicked.
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	/**
	 * 
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode == RESULT_OK) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, intent);

			/* acquire cipher text from QR Code */
			String cipherText = scanResult.getContents();

			/* acquire user password */
			EditText password = (EditText) findViewById(R.id.password);

			/* handle null password field */
			if (password.getText().toString().equals("")) {
				password.setText(generateRandomPassword(ALPHABET, 8));
			}

			/* TODO prompt users for response colors */

			/* generate salt values for symmetric key */
			byte[] salt = Crypto.saltShaker(password.getText().toString(),
					Color.BLUE, Color.RED, Color.GREEN);

			/*
			 * TODO replace Color.BLACK,Color.RED,Color.GREEN with user choices
			 */

			/* clears password */
			password.setText("");

			/* generate the plain text */
			String plainText = Crypto.decrypt(salt, cipherText);

			/* launches DisplayScannedQRCode activity to display plain text */
			Intent dipslayMessage = new Intent(this, DisplayScannedQRCode.class);
			dipslayMessage.putExtra(DISPLAY_MESSAGE, plainText);
			startActivity(dipslayMessage);
		} else if (resultCode == RESULT_CANCELED) {
			/* clears password */
			EditText password = (EditText) findViewById(R.id.password);
			password.setText("");

			/* Toast scan cancelled */
			Toast toast = Toast.makeText(this, "Scan was Cancelled!",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		}
	}

	/**
	 * @param alphabet
	 *            a String representing a list of valid chars to appear in the
	 *            String to be generated.
	 * @param length
	 *            an int representing the desired length of the String
	 * @return a random String with the specified length representing a default
	 *         password.
	 */
	private static String generateRandomPassword(String alphabet, int length) {
		Random rand = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = alphabet.charAt(rand.nextInt(alphabet.length()));
		}
		return new String(text);
	}
}
