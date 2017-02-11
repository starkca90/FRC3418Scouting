package Schedule;

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
