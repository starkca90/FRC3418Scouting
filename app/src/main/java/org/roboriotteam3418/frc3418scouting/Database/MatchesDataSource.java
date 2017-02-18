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

package org.roboriotteam3418.frc3418scouting.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.roboriotteam3418.frc3418scouting.Application.ScoutActivity;
import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.Entries.Entry;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This class is responsible for being the public face of the database. It converts
 * information to valid SQLite3 commands to update and select data from the database.
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
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
        if (database == null || !database.isOpen()) {
            open();
        }
    }

    /**
     * Responsible for creating new match in database. Returns Cursor of created
     * Match
     *
     * @return Cursor pointing to created Match
     */
    private Cursor createMatch() {

        checkDBOpen();

        // Insert new row
        String strSQLInsert = "INSERT INTO " + dbHelper.getTableName() + " DEFAULT VALUES;";
        database.execSQL(strSQLInsert);

        int cnt = getMatchesCount();

        // Initialize Team and alliance information
        String strSQLUpdate = "UPDATE " + dbHelper.getTableName() + " SET " +
                SQLiteHelper.getColumnTeam() + " = \"0\", " +
                SQLiteHelper.getColumnAlliance() + " = \"" + Match.Alliance.BLUE + "\"" +
                " WHERE " + dbHelper.getMatchColumn() + " = " + cnt + ";";

        database.execSQL(strSQLUpdate);

        // Get Cursor pointing to new row
        String strSQLQuery = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        return database.rawQuery(strSQLQuery, new String[] {String.valueOf(cnt)});
    }

    /**
     * Responsible for updating team information for a given match
     * @param team Team number to be stored in database
     * @param match Match number being updated
     */
    public void updateTeam(String team, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + SQLiteHelper.getColumnTeam() + " = \"" +
                team + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    /**
     * Responsible for updating alliance information for a given match
     * @param alliance Alliance to be stored in database
     * @param match Match number being updated
     */
    public void updateAlliance(String alliance, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + SQLiteHelper.getColumnAlliance() + " = \"" +
                alliance + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        checkDBOpen();

        database.execSQL(strSQL);
    }

    /**
     * Responsible for updating information about a match's entry
     * @param column Entry being updated
     * @param value New data to be stored about the entry
     * @param match Match number being updated
     */
    public void updateMatchEntry(String column, String value, int match) {
        checkMatchExists(match);

        String strSQL = "UPDATE " + dbHelper.getTableName() + " SET " + "\"" + column + "\"" + " = \"" +
                value + "\" WHERE " + dbHelper.getMatchColumn() + " = " + match;

        open();

        database.execSQL(strSQL);
    }

    /**
     * Responsible for counting the number of matches currently stored in the database
     * @return Count of matches
     */
    private int getMatchesCount() {
        String strSQL = "SELECT * FROM " + dbHelper.getTableName();

        Cursor cursor = database.rawQuery(strSQL, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

    /**
     * Responsible for verifying a match exists in the database. If the match does not exist,
     * Creates a new match until match does exist
     * @param match Match number to be verified it exists
     * @return Cursor pointing to desired match
     */
    private Cursor checkMatchExists(int match) {
        if(match == 0) {
            match = 1;
        }

        open();

        String strSQL = "SELECT * FROM " + dbHelper.getTableName() + " WHERE " +
                dbHelper.getMatchColumn() + " = ?";

        Cursor cursor;
        do {
            cursor = database.rawQuery(strSQL, new String[]{String.valueOf(match)});

            if (cursor == null || cursor.getCount() <= 0) {
                cursor = createMatch();
            }
        } while (cursor == null || cursor.getCount() <= 0);

        return cursor;
    }

    /**
     * Responsible obtaining match information from the database and updating application variables
     * used to display information to user
     * @param match Match number of desired match
     * @return Match object containing information of desired match
     */
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

    /**
     * Responsible for iterating through a node and reading information from a Cursor pointing
     * to a specific Match and updating score values
     * @param node Current node being processed
     * @param cursor Current match being processed
     */
    private void restoreNode(ArrayList node, Cursor cursor) {
        for (int i = 0; i < node.size(); i++) {
            Entry curEntry = (Entry) node.get(i);
            int columnIndex = cursor.getColumnIndex(curEntry.getSQLColumn());
            curEntry.setValue(cursor.getString(columnIndex));
        }
    }

    /**
     * Responsible for dumping data from database to a csv file that can be further
     * manipulated
     *
     * @param outputDir Desired location of csv file to be stored to
     * @param filename  Name of csv file
     */
    public void outputDatabase(String outputDir, String filename) {
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

            // Go until all matches are processed
            while (curCSV.moveToNext()) {
                // Temporary store of match data
                String arrStr[] = new String[columnCnt];

                // Go until all columns are processed
                for (int i = 0; i < columnCnt; i++) {
                    arrStr[i] = curCSV.getString((i));
                }
                // Write temporary store to file
                csvWrite.writeNext(arrStr);
            }
            // Close file
            csvWrite.close();
            // Close cursor
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    /**
     * Responsible for pre-populating team and alliance information of a given match
     *
     * @param match    Match number to be pre-populated
     * @param team     Team being watched this match
     * @param alliance Alliance team is part of
     */
    public void prefillMatch(int match, String team, String alliance) {
        updateTeam(team, match);
        updateAlliance(alliance, match);
    }

    /**
     * Responsible for being public face of upgrading table
     */
    public void upgradeTable() {
        checkDBOpen();
        dbHelper.onUpgrade(database, 0, 1);
    }

}
