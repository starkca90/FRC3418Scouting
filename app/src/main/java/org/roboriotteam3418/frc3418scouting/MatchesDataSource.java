package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import ScoutingUI.Entry;
import au.com.bytecode.opencsv.CSVWriter;

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
        if (database == null || !database.isOpen()) {
            open();
        }
    }

    private Cursor createMatch() {

        checkDBOpen();

        String strSQLInsert = "INSERT INTO " + dbHelper.getTableName() + " DEFAULT VALUES;";
        database.execSQL(strSQLInsert);

        int cnt = getMatchesCount();

        String strSQLUpdate = "UPDATE " + dbHelper.getTableName() + " SET " +
                SQLiteHelper.getColumnTeam() + " = \"0\", " +
                SQLiteHelper.getColumnAlliance() + " = \"" + Match.Alliance.BLUE + "\"" +
                " WHERE " + dbHelper.getMatchColumn() + " = " + cnt + ";";

        database.execSQL(strSQLUpdate);

        String strSQLQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        return database.rawQuery(strSQLQuery, new String[] {String.valueOf(cnt)});
    }

    public void updateTeam(String team, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + SQLiteHelper.getColumnTeam() + " = \"" +
                team + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    public void updateAlliance(String alliance, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + SQLiteHelper.getColumnAlliance() + " = \"" +
                alliance + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    public void updateMatchEntry(String column, String value, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + column + " = \"" +
                value + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        open();

        database.execSQL(strSQL);
    }

    private int getMatchesCount() {
        String strSQL = "SELECT * FROM " + dbHelper.getTableName();

        Cursor cursor = database.rawQuery(strSQL, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

    private Cursor checkMatchExists(int match) {
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

        return cursor;
    }

    public Match loadMatch(int match) {
        Match retMatch;

        Cursor cursor = checkMatchExists(match);

        cursor.moveToFirst();

        retMatch = Match.getMatch();

        int matchValue = cursor.getInt(cursor.getColumnIndex(dbHelper.getMatchColumn()));
        String alliance = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getColumnAlliance()));
        String team = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getColumnTeam()));

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

    void outputDatabase(String outputDir, String filename) {
        String sqlQuery = "SELECT * FROM " + dbHelper.getTableName();
        File exportDir = new File(Environment.getExternalStorageDirectory(), outputDir);

        checkDBOpen();

        if (!exportDir.exists())
            exportDir.mkdirs();

        File file = new File(exportDir, filename);
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = database.rawQuery(sqlQuery, null);
            int columnCnt = curCSV.getColumnCount();

            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = new String[columnCnt];
                for (int i = 0; i < columnCnt; i++) {
                    arrStr[i] = curCSV.getString((i));
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    void prefillMatch(int match, String team, String alliance) {
        updateTeam(team, match);
        updateAlliance(alliance, match);
    }

    void upgradeTable() {
        checkDBOpen();
        dbHelper.onUpgrade(database, 0, 1);
    }

}
