package ScoutingUI;

/**
 * Created by cstark on 12/13/2016.
 */

public class Entry {
    public final String name;
    public final EventType type;
    public int value = 0;
    public final String[] options;
    public final String image;

    public enum EventType {BOOL, INT, MC, ERROR}

    ;

    public Entry(String name, EventType type, String value, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.image = image;

        this.options = null;
    }

    public Entry(String name, EventType type, String value, String options, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.image = image;
        this.options = options.split("\\s*,\\s*");
    }

}
