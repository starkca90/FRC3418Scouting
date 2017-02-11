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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import org.roboriotteam3418.frc3418scouting.Application.ScoutActivity;
import org.roboriotteam3418.frc3418scouting.Entries.Entry;

import java.util.ArrayList;

/**
 * This class is responsible for helping with SQLite operations. Contains information about
 * table, constant columns and database name.
 * <p>
 * This class should only be accessed by MatchesDataSource to control who can read/write
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
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

    /**
     * Responsible for building SQLite command to create a table containing columns for
     * each Entry
     * @return SQLite command to create table
     */
    private String buildCreateTable() {

        return ("CREATE TABLE " + TABLE_MATCHES + "( ") +
                getConstantColumns() +
                createNodeColumns(((ScoutActivity) context).getAutoElements()) +
                ", " +
                createNodeColumns(((ScoutActivity) context).getTeleElements()) +
                ");";
    }

    /**
     * Responsible for building part of the SQLite command to create a new table.
     * Contains the columns that a the same no matter what year.
     * @return partial SQLite command to create table
     */
    private String getConstantColumns() {
        return (COLUMN_MATCH + " INTEGER PRIMARY KEY AUTOINCREMENT, ") +
                COLUMN_TEAM + " TEXT DEFAULT \"0\", " +
                COLUMN_ALLIANCE + " TEXT DEFAULT \"BLUE\", ";
    }

    /**
     * Responsible for building part of the SQLite command to create a new table.
     * Contains the columns that may/do change each year
     * @param node Contains list of current node being generated
     * @return Partial SQLite command to create table
     */
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
