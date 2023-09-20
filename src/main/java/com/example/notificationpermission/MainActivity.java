package com.example.notificationpermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    String[] permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS};            //string store which permission to check from manifest
    boolean permission_post_notification = false;                                           //to check notification permission is given or not
    private Button btn1;
    private TextView txt1;
    private final ActivityResultLauncher<String> requestPermissionLauncherNotification = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {    // requesting to launch notification permission
        if (isGranted) {                                                                    //here checking notification permission granted or not
            permission_post_notification = true;                                            //performing task after permission granted
        } else {
            permission_post_notification = false;
            showPermissionDialog("Notification permission");                   //permission not granted asking for dialog to move to setting or exit
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn1 = findViewById(R.id.btn1);
        txt1 = findViewById(R.id.txt1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {       //checking android version to ask permission in Api 34 it is mandatory to ask permission
                    if (!permission_post_notification) {
                        requestPermissionNotification();                                      //requesting procedure to get permission
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void requestPermissionNotification() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {    //checking permission granted or not
            permission_post_notification = true;                                              //permission granted so setting permission status to true
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            requestPermissionLauncherNotification.launch(permissions[0]);                     //asking to launch permission request
        }
    }

    public void showPermissionDialog(String permission_desc) {                                 // created a dialog to ask to move to setting page or exit
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Alert")
                .setIcon(R.drawable.img1)
                .setMessage("Allow notification in settings ")
                .setPositiveButton(" Settings ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent rintent = new Intent();
                        rintent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        rintent.setData(uri);
                        startActivity(rintent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(" Exit ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .show();
    }

}