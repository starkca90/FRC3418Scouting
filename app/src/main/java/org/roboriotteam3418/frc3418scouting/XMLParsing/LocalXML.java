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

package org.roboriotteam3418.frc3418scouting.XMLParsing;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This class is responsible for getting an XML file from the local device
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class LocalXML {

    private static LocalXML singleton;
    private String tag = this.getClass().toString();

    public static LocalXML GetXML() {
        if (singleton == null) {
            singleton = new LocalXML();
        }

        return singleton;
    }

    /**
     * Responsible for reading a file from the path and getting it into an InputStream
     * for further manipulation
     *
     * @param path Local path to desired file
     * @return InputStream of desired file
     */
    public InputStream getXML(String path) {
        try {

            // Open the file
            BufferedReader xmlFile = new BufferedReader(new FileReader(path));

            // Buffer the file
            char[] charBuffer = new char[8 * 1024];
            StringBuilder builder = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = xmlFile.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }

            // Stream for further manipulation
            InputStream targetStream = new ByteArrayInputStream(
                    builder.toString().getBytes(StandardCharsets.UTF_8));

            // File in stream, close the file
            xmlFile.close();

            return targetStream;

        } catch (FileNotFoundException e) {
            Log.e(tag, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

