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

package org.roboriotteam3418.frc3418scouting.DataStructures;

/**
 * This class is responsible for storing information about the schedule for the given recorder
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class ScheduleEntry {

    private final String team;
    private final String alliance;

    public ScheduleEntry(String team, String alliance) {
        this.team = team;
        this.alliance = alliance;
    }

    public String getTeam() {
        return team;
    }

    public String getAlliance() {
        return alliance;
    }
}
