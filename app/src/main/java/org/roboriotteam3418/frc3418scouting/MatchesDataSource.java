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

    public Match createMatch() {

        checkDBOpen();

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
        String strSQL = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        Cursor matchRow = database.rawQuery(strSQL, new String[] {String.valueOf(match)});

        if(matchRow != null) {
            Match newMatch = new Match();
            newMatch.setMatch(match);

            restoreNode(((ScoutActivity) context).getAutoElements(), matchRow);
            restoreNode(((ScoutActivity) context).getTeleElements(), matchRow);

            return newMatch;
        }

        return null;
    }

    private void restoreNode(ArrayList node, Cursor cursor) {
        for (int i = 0; i < node.size(); i++) {
            Entry curEntry = (Entry) node.get(i);
            int columnIndex = cursor.getColumnIndex(curEntry.getSQLColumn());
            curEntry.setValue(cursor.getString(columnIndex));
        }
    }

}
