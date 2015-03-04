package edu.aims.mitchell.ian.fortunecookie;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.aims.mitchell.ian.fortunecookie.DatabaseContract.FortuneEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "fortune.db";
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = " , ";
	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + FortuneEntry.TABLE_NAME + " (" +
					FortuneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_FORTUNE + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_ENGLISH + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_CHINESE + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_PRO + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LAT + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LONG + TEXT_TYPE +
					" )";
	private static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + FortuneEntry.TABLE_NAME;
	private final String LOG_TAG = DatabaseHelper.class.getSimpleName();

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(LOG_TAG, "Helper Initialized");
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
		Log.d(LOG_TAG, "New Database Created");

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	public void add(Fortune fortune) {

		Log.d("Adding Fortune to DB", "Yep");

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FortuneEntry.COLUMN_NAME_FORTUNE, fortune.fortune);
		values.put(FortuneEntry.COLUMN_NAME_ENGLISH, fortune.english);
		values.put(FortuneEntry.COLUMN_NAME_CHINESE, fortune.chinese);
		values.put(FortuneEntry.COLUMN_NAME_PRO, fortune.pro);
		values.put(FortuneEntry.COLUMN_NAME_LAT, fortune.lat);
		values.put(FortuneEntry.COLUMN_NAME_LONG, fortune.lon);


		db.insert(
				DatabaseContract.FortuneEntry.TABLE_NAME,
				null,
				values);
		db.close();


	}
}
