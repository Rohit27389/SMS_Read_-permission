package com.example.sms_contact_provider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private Button sms;
    private static int SMS_PERMISSION_CODE = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sms = findViewById(R.id.sms_permission);
        smsClick();
    }

    public void smsClick() {
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.READ_SMS, SMS_PERMISSION_CODE);
            }
        });
    }

    public void readSMS() {
        ArrayList<String> smsArrayList = new ArrayList();
        ContentResolver mContentResolver = getContentResolver();
        // Uri uri = Uri.parse("content://sms/index");
        Uri uri = Uri.parse("content://sms/");
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = mContentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
        int body = cursor.getColumnIndex("body");
        int address = cursor.getColumnIndex("address");
        while (cursor.moveToNext()) {
            String smsData = "SMS From " + cursor.getString(address) + ": " + cursor.getString(body);
            smsArrayList.add(smsData);
        }
        for (int i = 0; i < smsArrayList.size(); i++) {
            System.out.println(i + ">>" + smsArrayList.get(i));
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            readSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
                readSMS();
            } else {
                Toast.makeText(MainActivity.this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}