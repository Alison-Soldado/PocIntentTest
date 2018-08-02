package com.example.android.pocintenttest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTypePhoneNumber;

    private static final int REQUEST_PHONE_CALL = 15;
    private static final int REQUEST_CODE_PICK = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        editTextTypePhoneNumber = findViewById(R.id.activity_main_text_type_number);
    }

    public void goToOther(View view) {
        Intent intentOther = new Intent(this, OtherActivity.class);
        intentOther.putExtra("name", "Alison");
        startActivity(intentOther);
    }

    public void goToBrowser(View view) {
        Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.br"));
        startActivity(intentBrowser);
    }

    public void goToCall(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(editTextTypePhoneNumber.getText().toString()))));
        }
    }

    public void goToCallPickNumber(View view) {
        Intent pickContactIntent = new Intent(this, ContactActivity.class);
        startActivityForResult(pickContactIntent, REQUEST_CODE_PICK);
    }

    public void clearField(View view) {
        editTextTypePhoneNumber.getText().clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:".concat(editTextTypePhoneNumber.getText().toString()))));
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK) {
            if (resultCode == RESULT_OK) {
                editTextTypePhoneNumber.setText(data.getExtras().getString(ContactActivity.KEY_PHONE_NUMBER));
            }
        }
    }
}
