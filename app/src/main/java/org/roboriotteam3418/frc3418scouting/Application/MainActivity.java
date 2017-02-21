/*
 * Copyright (c) 2017. RoboRiot and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of RoboRiot Scouting.
 *
 * RoboRiot Scouting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RoboRiot Scouting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RoboRiot Scouting.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.roboriotteam3418.frc3418scouting.Application;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.roboriotteam3418.frc3418scouting.R;

import Permissions.RuntimePermissionsActivity;

/**
 * This class is responsible for being the first activity that runs when
 * the application is launched. This activity is responsible for ensuring the
 * required permissions are available before running the main application.
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */

public class MainActivity extends RuntimePermissionsActivity {

    private final int REQUEST_PERMISSIONS = 20;

    /**
     * Responsible for setting the button listener and sending the request for the permissions
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set button listener
        Button btnGrant = (Button) findViewById(R.id.btnRequest);
        btnGrant.setOnClickListener(v -> MainActivity.super.requestAppPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.txRuntimePermissions,
                REQUEST_PERMISSIONS));

        // Request both read and write permissions to external storage
        MainActivity.super.requestAppPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.txRuntimePermissions,
                REQUEST_PERMISSIONS);
    }

    /**
     * Responsible for handling the response when permissions were granted
     * @param requestCode
     */
    @Override
    public void onPermissionsGranted(int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();

        // Necessary permissions attained, moving on to the main attraction
        startActivity(new Intent(this, ScoutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}
