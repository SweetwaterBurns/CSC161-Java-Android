package edu.aims.mitchell.ian.fortunecookie;

import android.content.Context;

import static edu.aims.mitchell.ian.fortunecookie.R.string.chinese_label;
import static edu.aims.mitchell.ian.fortunecookie.R.string.chinese_text;
import static edu.aims.mitchell.ian.fortunecookie.R.string.english_label;
import static edu.aims.mitchell.ian.fortunecookie.R.string.english_text;
import static edu.aims.mitchell.ian.fortunecookie.R.string.language_label;
import static edu.aims.mitchell.ian.fortunecookie.R.string.pro_label;
import static edu.aims.mitchell.ian.fortunecookie.R.string.pro_text;

/**
 * Created by Ian on 1/19/2015.
 */
public class Lang {

	private static String[] l = new String[7];

	public Lang(Context ctx) {
		l[0] = ctx.getString(language_label);
		l[1] = ctx.getString(english_label);
		l[2] = ctx.getString(english_text);
		l[3] = ctx.getString(chinese_label);
		l[4] = ctx.getString(chinese_text);
		l[5] = ctx.getString(pro_label);
		l[6] = ctx.getString(pro_text);
	}

	public static String[] random() {
		return l;
	}
}
