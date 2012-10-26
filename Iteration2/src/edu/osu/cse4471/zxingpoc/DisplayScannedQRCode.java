package edu.osu.cse4471.zxingpoc;

import android.os.Bundle;
import android.app.Activity;

public class DisplayScannedQRCode extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scanned_qrcode);
    }
}
