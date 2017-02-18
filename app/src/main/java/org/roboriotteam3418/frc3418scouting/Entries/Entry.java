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

package org.roboriotteam3418.frc3418scouting.Entries;

import android.content.Context;
import android.util.SparseArray;
import android.widget.RelativeLayout;

/**
 * This class is responsible for being the base class of each Entry type. Contains the
 * basic information that is common between each Entry type
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public abstract class Entry {
    private static final SparseArray<EventType> intToTypeMap = new SparseArray<>();

    static {
        for(EventType type : EventType.values()) {
            intToTypeMap.put(type.ordinal(), type);
        }
    }

    private final String name;
    private final EventType type;
    private final String[] options;
    private final String image;
    private final String defaultValue;
    private int value = 0;

    /**
     * Responsible for creating an Entry that does not contain options
     *
     * @param name  Entry name
     * @param type  Type of Entry
     * @param value Initial value of Entry
     * @param image Image to help with Entry description
     */
    public Entry(String name, EventType type, String value, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.defaultValue = value;
        this.image = image;

        this.options = null;
    }

    /**
     * Responsible for creating an Entry that contains options
     * @param name Entry name
     * @param type Type of entry
     * @param value Initial value of Entry
     * @param options Comma separated options for the Entry
     * @param image Image to help with Entry description
     */
    public Entry(String name, EventType type, String value, String options, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.defaultValue = value;
        this.image = image;
        this.options = options.split("\\s*,\\s*");
    }

    /**
     * Responsible for converting a string representation of a type to the
     * actual type of event
     * @param type String representation of type
     * @return EventType of event
     */
    public static Entry.EventType getEventType(String type) {
        Entry.EventType retVal = Entry.EventType.ERROR;

        switch (type) {
            case "INT":
                retVal = EventType.INT;
                break;
            case "BOOL":
                retVal = EventType.BOOL;
                break;
            case "MC":
                retVal = EventType.MC;
                break;
        }

        return retVal;
    }

    @Override
    public String toString() {
        String imaValue = " ";
        if(image != null)
            imaValue = image;

        return name + "," +
                type + "," +
                value + "," +
                imaValue  +
                optionsToString();
    }

    /**
     * Responsible for converting options back to a comma separated string
     * @return Comma separated string of options
     */
    private String optionsToString() {
        if(options != null) {
            int iMax = options.length - 1;

            StringBuilder b = new StringBuilder();
            b.append(",");
            for (int i = 0; ; i++) {
                b.append(String.valueOf(options[i]));
                if (i == iMax)
                    return b.toString();
                b.append(",");
            }
        } else {
            return "";
        }
    }

    public String getSQLCreate() {
        return "\"" + name + "\"" + " TEXT NOT NULL DEFAULT \"" + defaultValue + "\"";
    }

    public String getSQLColumn() {
        return name;
    }

    public abstract RelativeLayout createLayout(Context context);

    public abstract RelativeLayout getLayout();

    public abstract void setValue(String value);

    public enum EventType {BOOL, INT, MC, ERROR}

}
