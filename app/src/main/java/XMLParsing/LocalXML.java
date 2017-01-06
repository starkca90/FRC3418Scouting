package XMLParsing;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private static Activity _context;

    public static LocalXML GetXML(Activity context) {
        if (singleton == null) {
            singleton = new LocalXML();
        }
        _context = context;
        return singleton;
    }

    public InputStream getXML(String path) {
        try {

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(_context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(_context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                }
                ActivityCompat.requestPermissions((MainActivity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_AND_WRITE_SDK);
            } else {


                BufferedReader xmlFile = new BufferedReader(new FileReader(path));

                char[] charBuffer = new char[8 * 1024];
                StringBuilder builder = new StringBuilder();
                int numCharsRead;
                while ((numCharsRead = xmlFile.read(charBuffer, 0, charBuffer.length)) != -1) {
                    builder.append(charBuffer, 0, numCharsRead);
                }

                InputStream targetStream = new ByteArrayInputStream(
                        builder.toString().getBytes(StandardCharsets.UTF_8));

                xmlFile.close();

                return targetStream;
            }
        } catch (FileNotFoundException e) {
            Log.e(tag, e.getStackTrace().toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

