package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import ScoutingUI.Entry;

/**
 * Created by caseystark on 1/20/17.
 */

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

    public String getTableName() {
        return TABLE_MATCHES;
    }

    public String getMatchColumn() {
        return COLUMN_MATCH;
    }

    public static String getColumnTeam() {
        return COLUMN_TEAM;
    }

    public static String getColumnAlliance() {
        return COLUMN_ALLIANCE;
    }

    private String buildCreateTable() {
        StringBuilder b = new StringBuilder();

        b.append("CREATE TABLE " + TABLE_MATCHES + "( ");
        b.append(getConstantColumns());

        b.append(createNodeColumns(((ScoutActivity) context).getAutoElements()));
        b.append(", ");

        b.append(createNodeColumns(((ScoutActivity) context).getTeleElements()));
        b.append(");");

        return b.toString();
    }

    private String getConstantColumns() {
        StringBuilder b = new StringBuilder();

        b.append(COLUMN_MATCH + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        b.append(COLUMN_TEAM + " TEXT DEFAULT \"\", ");
        b.append(COLUMN_ALLIANCE + " TEXT DEFAULT \"\", ");

        return b.toString();
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
