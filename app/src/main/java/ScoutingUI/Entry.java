package ScoutingUI;

/**
 * Created by cstark on 12/13/2016.
 */

public class Entry {
    public final String name;
    public final EventType type;
    public final ArrayList value;
    public final String image;

    public enum EventType {TF, TALLY, MULTI};

    private Entry(String name, EventType type, String value, String image) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.image = image;
    }

}
