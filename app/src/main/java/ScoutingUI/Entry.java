package ScoutingUI;

import android.content.Context;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cstark on 12/13/2016.
 */

public abstract class Entry {
    private static final Map<Integer, EventType> intToTypeMap = new HashMap<Integer, EventType>();

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

    public Entry(String components) {
        String[] comArray = components.split("\\s*,\\s*");

        this.name = comArray[0];
        this.type = intToTypeMap.get(Integer.valueOf(Integer.getInteger(comArray[1])));
        this.value = Integer.getInteger(comArray[2]);
        this.defaultValue = comArray[2];
        this.image = comArray[3];

        if(comArray.length > 4) {
            ArrayList optionsList = new ArrayList();
            for (int i = 4; i < comArray.length; i++) {
                optionsList.add(i);
            }

            this.options = (String[]) optionsList.toArray();
        } else {
            this.options = null;
        }
    }

    public static Entry.EventType getEventType(String type) {
        Entry.EventType retVal = Entry.EventType.ERROR;

        if (type.equals("INT"))
            retVal = Entry.EventType.INT;
        else if (type.equals("BOOL"))
            retVal = Entry.EventType.BOOL;
        else if (type.equals("MC"))
            retVal = Entry.EventType.MC;

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

    public abstract RelativeLayout createLayout(Context context);

    public abstract RelativeLayout getLayout();

    public abstract void setValue(String value);

    public enum EventType {BOOL, INT, MC, ERROR}

}
