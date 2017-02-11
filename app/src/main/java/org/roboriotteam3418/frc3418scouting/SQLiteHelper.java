package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import ScoutingUI.Entry;

class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TABLE_MATCHES = "matches";
    private static final String COLUMN_MATCH = "_match";
    private static final String COLUMN_TEAM = "team";
    private static final String COLUMN_ALLIANCE = "alliance";
    private static final String DATABASE_NAME = "matches.db";
    private static final int DATABASE_VERSION = 1;

    private final Context context;

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(buildCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        onCreate(db);
    }

    String getTableName() {
        return TABLE_MATCHES;
    }

    String getMatchColumn() {
        return COLUMN_MATCH;
    }

    static String getColumnTeam() {
        return COLUMN_TEAM;
    }

    static String getColumnAlliance() {
        return COLUMN_ALLIANCE;
    }

    private String buildCreateTable() {

        return ("CREATE TABLE " + TABLE_MATCHES + "( ") +
                getConstantColumns() +
                createNodeColumns(((ScoutActivity) context).getAutoElements()) +
                ", " +
                createNodeColumns(((ScoutActivity) context).getTeleElements()) +
                ");";
    }

    private String getConstantColumns() {
        return (COLUMN_MATCH + " INTEGER PRIMARY KEY AUTOINCREMENT, ") +
                COLUMN_TEAM + " TEXT DEFAULT \"\", " +
                COLUMN_ALLIANCE + " TEXT DEFAULT \"\", ";
    }

    @NonNull
    private String createNodeColumns(ArrayList node) {
        StringBuilder b = new StringBuilder();

        for(int i = 0; i < node.size(); i++) {
            Entry curEntry = (Entry) node.get(i);
            b.append(curEntry.getSQLCreate());

            if(i != (node.size() - 1)) {
                b.append(", ");
            }
        }

        return b.toString();
    }
}
