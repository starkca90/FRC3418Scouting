package XMLParsing;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.roboriotteam3418.frc3418scouting.MainActivity;
import org.roboriotteam3418.frc3418scouting.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by cstark on 12/13/2016.
 */

public class LocalXML {

    private String tag = this.getClass().toString();

    private static LocalXML singleton;

    public static LocalXML GetXML() {
        if (singleton == null) {
            singleton = new LocalXML();
        }

        return singleton;
    }

    public InputStream getXML(AppCompatActivity caller, String path) {
        try {

            if(ContextCompat.checkSelfPermission(caller, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(caller, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show and explanation to the user *asyncronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission
                } else {
                    // No explination needed, we can request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }

            // Open the file
            BufferedReader xmlFile = new BufferedReader(new FileReader(path));

            // Buffer the file
            char[] charBuffer = new char[8 * 1024];
            StringBuilder builder = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = xmlFile.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }

            // Stream for further manipulation
            InputStream targetStream = new ByteArrayInputStream(
                    builder.toString().getBytes(StandardCharsets.UTF_8));

            //
            xmlFile.close();

            return targetStream;

        } catch (FileNotFoundException e) {
            Log.e(tag, e.getStackTrace().toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

