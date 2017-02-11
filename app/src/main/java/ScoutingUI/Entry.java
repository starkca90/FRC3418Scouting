package ScoutingUI;

import android.content.Context;
import android.util.SparseArray;
import android.widget.RelativeLayout;

public abstract class Entry {
    private static final SparseArray<EventType> intToTypeMap = new SparseArray<>();

    static {
        for(EventType type : EventType.values()) {
            intToTypeMap.put(type.ordinal(), type);
        }
    }

    private final String name;
    private final EventType type;
    private final String[] options;
    private final String image;
    private final String defaultValue;
    private int value = 0;

    public Entry(String name, EventType type, String value, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.defaultValue = value;
        this.image = image;

        this.options = null;
    }

    public Entry(String name, EventType type, String value, String options, String image) {
        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.defaultValue = value;
        this.image = image;
        this.options = options.split("\\s*,\\s*");
    }

    public static Entry.EventType getEventType(String type) {
        Entry.EventType retVal = Entry.EventType.ERROR;

        switch (type) {
            case "INT":
                retVal = EventType.INT;
                break;
            case "BOOL":
                retVal = EventType.BOOL;
                break;
            case "MC":
                retVal = EventType.MC;
                break;
        }

        return retVal;
    }

    @Override
    public String toString() {
        String imaValue = " ";
        if(image != null)
            imaValue = image;

        return name + "," +
                type + "," +
                value + "," +
                imaValue  +
                optionsToString();
    }

    private String optionsToString() {
        if(options != null) {
            int iMax = options.length - 1;

            StringBuilder b = new StringBuilder();
            b.append(",");
            for (int i = 0; ; i++) {
                b.append(String.valueOf(options[i]));
                if (i == iMax)
                    return b.toString();
                b.append(",");
            }
        } else {
            return "";
        }
    }

    public String getSQLCreate() {
        return name + " TEXT NOT NULL DEFAULT \"" + defaultValue + "\"";
    }

    public String getSQLColumn() {
        return name;
    }

    public abstract RelativeLayout createLayout(Context context);

    public abstract RelativeLayout getLayout();

    public abstract void setValue(String value);

    public enum EventType {BOOL, INT, MC, ERROR}

}
