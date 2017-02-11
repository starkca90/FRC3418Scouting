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
 * This class is responsible for storing data about the current match. Information includes
 * the current match, team and alliance of the team.
 * <p>
 * This class also stores the name of the recorder of the device
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class Match {


    private String team;
    private Alliance alliance;
    private int match;
    private String recorder;

    private static Match singleton;

    private Match() {
        this.match = 1;
        this.team = "";
        this.alliance = Alliance.BLUE;
    }

    public static Match getMatch() {
        if (singleton == null)
            singleton = new Match();

        return singleton;
    }

    /**
     * Respoinsible for updating current match with new information
     *
     * @param team     New team being souted
     * @param alliance Alliance of team
     * @param match    Match number of new match
     * @return Reference to current match
     */
    public Match loadMatch(String team, String alliance, int match) {
        this.team = team;
        if(alliance.equals("BLUE"))
            this.alliance = Alliance.BLUE;
        else
            this.alliance = Alliance.RED;

        this.match = match;

        return this;
    }

    public int getMatchNumber() { return match; }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getRecorder() {
        return this.recorder;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public enum Alliance {BLUE, RED, ERROR}


}
