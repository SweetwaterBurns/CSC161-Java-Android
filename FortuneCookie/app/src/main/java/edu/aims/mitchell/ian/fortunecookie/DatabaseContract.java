package edu.aims.mitchell.ian.fortunecookie;

import android.provider.BaseColumns;

public final class DatabaseContract {
	// To prevent someone from accidentally instantiating the contract class,
	// give it an empty constructor.

	public DatabaseContract() {
	}

	/* Inner class that defines the table contents */
	public static abstract class FortuneEntry implements BaseColumns {
		public static final String TABLE_NAME = "fortune";
		public static final String COLUMN_NAME_FORTUNE = "fortune";
		public static final String COLUMN_NAME_ENGLISH = "english";
		public static final String COLUMN_NAME_CHINESE = "chinese";
		public static final String COLUMN_NAME_PRO = "pro";
		public static final String COLUMN_NAME_LAT = "latitude";
		public static final String COLUMN_NAME_LONG = "longitude";
		public static final String COLUMN_NAME_LOTTO1 = "lotto1";
		public static final String COLUMN_NAME_LOTTO2 = "lotto2";
		public static final String COLUMN_NAME_LOTTO3 = "lotto3";
		public static final String COLUMN_NAME_LOTTO4 = "lotto4";
		public static final String COLUMN_NAME_LOTTO5 = "lotto5";
		public static final String COLUMN_NAME_LOTTO6 = "lotto6";
	}
}
