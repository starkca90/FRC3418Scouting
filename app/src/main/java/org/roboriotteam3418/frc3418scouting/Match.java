package org.roboriotteam3418.frc3418scouting;

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

    Match loadMatch(String team, String alliance, int match) {
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;

    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    String getRecorder() {
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
