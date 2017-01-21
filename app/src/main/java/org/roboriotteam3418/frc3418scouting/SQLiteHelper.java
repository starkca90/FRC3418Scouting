package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by caseystark on 1/20/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_MATCH = "_match";
    public static final String COLUMN_TEAM = "team";
    public static final String COLUMN_ALLIANCE = "alliance";
    private static final String DATABASE_NAME = "matches.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        onCreate(db);
    }

    public String getTableName() {
        return TABLE_MATCHES;
    }

    public String getMatchColumn() {
        return COLUMN_MATCH;
    }
}
