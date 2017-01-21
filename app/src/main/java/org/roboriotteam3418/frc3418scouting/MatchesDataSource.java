package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by caseystark on 1/20/17.
 */

public class MatchesDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private ArrayList allColumns;

    public MatchesDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Match createMatch() {
        String strSQL = "INSERT INTO " + dbHelper.getTableName() + " DEFAULT VALUES;";
        database.execSQL(strSQL);

        return new Match();
    }

    public void updateMatchEntry(String column, String value, int match) {
        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + column + " = " +
                value + " WHERE " + dbHelper.getMatchColumn() + " = " + match;

        database.execSQL(strSQL);
    }

    public Match loadMatch(int match) {
        
    }
}
