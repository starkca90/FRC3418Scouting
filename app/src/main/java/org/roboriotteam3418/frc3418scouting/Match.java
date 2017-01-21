package org.roboriotteam3418.frc3418scouting;

/**
 * Created by caseystark on 1/20/17.
 */

public class Match {


    private String team;
    private Alliance alliance;
    private int match;

    public Match() {

    }

    public int getMatch() { return match; }

    public void setMatch(int match) {
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
