package org.roboriotteam3418.frc3418scouting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import Permissions.RuntimePermissionsActivity;

public class MainActivity extends RuntimePermissionsActivity {

    private final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGrant = (Button) findViewById(R.id.btnRequest);
        btnGrant.setOnClickListener(v -> MainActivity.super.requestAppPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt,
                REQUEST_PERMISSIONS));

        MainActivity.super.requestAppPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.runtime_permissions_txt,
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, ScoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}
