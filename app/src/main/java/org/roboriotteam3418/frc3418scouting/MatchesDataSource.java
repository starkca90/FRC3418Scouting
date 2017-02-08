package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import ScoutingUI.Entry;


/**
 * Created by caseystark on 1/20/17.
 */

public class MatchesDataSource {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private Context context;

    private static MatchesDataSource mds;

    private MatchesDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
        this.context = context;
    }

    public static MatchesDataSource getMDS(Context context) {
        if (mds == null) {
            mds = new MatchesDataSource(context);
        }

        return mds;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void checkDBOpen() {
        if(!database.isOpen()) {
            open();
        }
    }

    public Cursor createMatch() {

        checkDBOpen();

        String strSQLInsert = "INSERT INTO " + dbHelper.getTableName() + " DEFAULT VALUES;";
        database.execSQL(strSQLInsert);

        int cnt = getMatchesCount();

        String strSQLUpdate = "UPDATE " + dbHelper.getTableName() + " SET " +
                dbHelper.getColumnTeam() + " = \"0\", " +
                dbHelper.getColumnAlliance() + " = \"" + Match.Alliance.BLUE + "\"" +
                " WHERE " + dbHelper.getMatchColumn() + " = " + cnt + ";";

        database.execSQL(strSQLUpdate);

        String strSQLQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        return database.rawQuery(strSQLQuery, new String[] {String.valueOf(cnt)});
    }

    public void updateTeam(String team, int match) {
        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + dbHelper.getColumnTeam() + " = \"" +
                team + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    public void updateAlliance(String alliance, int match) {
        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + dbHelper.getColumnAlliance() + " = \"" +
                alliance + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    public void updateMatchEntry(String column, String value, int match) {
        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + column + " = \"" +
                value + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        open();

        database.execSQL(strSQL);
    }

    private int getMatchesCount() {
        String strSQL = "SELECT * FROM " + dbHelper.getTableName();

        Cursor cursor = database.rawQuery(strSQL, null);
        int cnt = cursor.getCount();

        return cnt;
    }

    public Match loadMatch(int match) {
        Match retMatch;

        if(match == 0) {
            match = 1;
        }

        open();

        String strSQL = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        Cursor cursor = database.rawQuery(strSQL, new String[] {String.valueOf(match)});

        if(cursor == null || cursor.getCount() <= 0) {
            cursor = createMatch();
        }

        cursor.moveToFirst();

        retMatch = Match.getMatch();

        int matchValue = cursor.getInt(cursor.getColumnIndex(dbHelper.getMatchColumn()));
        String alliance = cursor.getString(cursor.getColumnIndex(dbHelper.getColumnAlliance()));
        String team = cursor.getString(cursor.getColumnIndex(dbHelper.getColumnTeam()));

        retMatch.loadMatch(team, alliance, matchValue);

        restoreNode(((ScoutActivity) context).getAutoElements(), cursor);
        restoreNode(((ScoutActivity) context).getTeleElements(), cursor);

        return retMatch;
    }

    private void restoreNode(ArrayList node, Cursor cursor) {
        for (int i = 0; i < node.size(); i++) {
            Entry curEntry = (Entry) node.get(i);
            int columnIndex = cursor.getColumnIndex(curEntry.getSQLColumn());
            curEntry.setValue(cursor.getString(columnIndex));
        }
    }

}
