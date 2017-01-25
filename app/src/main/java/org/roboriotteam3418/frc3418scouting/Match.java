package org.roboriotteam3418.frc3418scouting;

/**
 * Created by caseystark on 1/20/17.
 */

public class Match {


    private String team;
    private Alliance alliance;
    private int match;

    private static Match singleton;

    public Match() {
        this.match = 1;
        this.team = "";
        this.alliance = Alliance.BLUE;
    }

    public Match loadMatch(String team, Alliance alliance, int match) {
        this.team = team;
        this.alliance = alliance;
        this.match = match;

        return this;
    }

    public Match loadMatch(String team, String alliance, int match) {
        this.team = team;
        if(alliance.equals("BLUE"))
            this.alliance = Alliance.BLUE;
        else
            this.alliance = Alliance.RED;

        this.match = match;

        return this;
    }

    public static Match getMatch() {
        if(singleton == null)
            singleton = new Match();

        return singleton;
    }

    public int getMatchNumber() { return match; }

    public void setMatchNumber(int match) {
        if(match == 0) {
            this.match = 1;
        } else
            this.match = match;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public enum Alliance {BLUE, RED, ERROR}


}
