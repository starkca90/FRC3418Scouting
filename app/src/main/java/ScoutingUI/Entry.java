package ScoutingUI;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by cstark on 12/13/2016.
 */

public abstract class Entry {
    private final String name;
    private final EventType type;
    private int value = 0;
    private final String[] options;
    private final String image;

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

    public abstract RelativeLayout getLayout(Context context);
    public abstract RelativeLayout getLayout();

}
