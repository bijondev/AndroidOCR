package com.bkb.placid.ocr.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;


public class MainActivity extends AppCompatActivity {

    public static final int MY_BLINKID_REQUEST_CODE = 123;

    private BlinkIdCombinedRecognizer recognizer;
    private RecognizerBundle recognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MicroblinkSDK.setLicenseFile("linkid/MB_com.bkb.placid.ocr.app_BlinkID_Android_2020-02-01.mblic", this);

        recognizer = new BlinkIdCombinedRecognizer();
        recognizerBundle = new RecognizerBundle(recognizer);

    }

    public void onScanButtonClick(View view) {
        // use default UI for scanning documents
        BlinkIdUISettings uiSettings = new BlinkIdUISettings(recognizerBundle);

        // start scan activity based on UI settings
        ActivityRunner.startActivityForResult(this, MY_BLINKID_REQUEST_CODE, uiSettings);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // onActivityResult is called whenever we returned from activity started with startActivityForResult
        // We need to check request code to determine that we have really returned from BlinkID activity
        if (requestCode != MY_BLINKID_REQUEST_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            // OK result code means scan was successful
            onScanSuccess(data);
        } else {
            // user probably pressed Back button and cancelled scanning
            onScanCanceled();
        }
    }

    private void onScanSuccess(Intent data) {
        // update recognizer results with scanned data
        recognizerBundle.loadFromIntent(data);

        // you can now extract any scanned data from result, we'll just get primary id
        BlinkIdCombinedRecognizer.Result result = recognizer.getResult();
        String name = result.getFullName();
        if (name.isEmpty()) {
            name = result.getFirstName();
        }

        System.out.println("Name: " + name);
        System.out.println("ID : " + result.getDocumentNumber());
        Toast.makeText(this, "Name: " + name, Toast.LENGTH_LONG).show();
    }


    private void onScanCanceled() {
        Toast.makeText(this, "Scan cancelled!", Toast.LENGTH_SHORT).show();
    }

    }
