package org.roboriotteam3418.frc3418scouting;

/**
 * Created by caseystark on 1/20/17.
 */

public class Match {


    private int team;
    private Alliance alliance;

    public Match() {

    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
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
