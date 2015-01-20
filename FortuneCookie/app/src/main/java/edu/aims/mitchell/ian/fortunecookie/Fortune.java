package edu.aims.mitchell.ian.fortunecookie;

import android.content.Context;

import static edu.aims.mitchell.ian.fortunecookie.R.string.fortune_label;
import static edu.aims.mitchell.ian.fortunecookie.R.string.fortune_text;

/**
 * Created by Ian on 1/19/2015.
 */
public class Fortune {
    private static String[] f = new String[2];

    public Fortune(Context ctx){
        f[0] = ctx.getString(fortune_label);//ctx.getString(fortune_label);
        f[1] = ctx.getString(fortune_text);
    }

    public static String[] random() {
        return f;
    }
}
