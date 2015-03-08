package edu.aims.mitchell.ian.fortunecookie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.aims.mitchell.ian.fortunecookie.DatabaseContract.FortuneEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.

	public static final int DATABASE_VERSION = 3;
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
					FortuneEntry.COLUMN_NAME_LONG + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO1 + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO2 + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO3 + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO4 + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO5 + TEXT_TYPE + COMMA_SEP +
					FortuneEntry.COLUMN_NAME_LOTTO6 + TEXT_TYPE +
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

		values.put(FortuneEntry.COLUMN_NAME_LOTTO1, fortune.lotto[0]);
		values.put(FortuneEntry.COLUMN_NAME_LOTTO2, fortune.lotto[1]);
		values.put(FortuneEntry.COLUMN_NAME_LOTTO3, fortune.lotto[2]);
		values.put(FortuneEntry.COLUMN_NAME_LOTTO4, fortune.lotto[3]);
		values.put(FortuneEntry.COLUMN_NAME_LOTTO5, fortune.lotto[4]);
		values.put(FortuneEntry.COLUMN_NAME_LOTTO6, fortune.lotto[5]);

		Log.d("Stored Lotto", fortune.lotto.toString());

		db.insert(
				DatabaseContract.FortuneEntry.TABLE_NAME,
				null,
				values);
		db.close();


	}


	public Fortune retrieve(int fortuneid) {
		Fortune loadedFortune = new Fortune();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cur = db.query(FortuneEntry.TABLE_NAME, null, null, null, null, null, null);
		cur.moveToPosition(fortuneid);

		loadedFortune.fortune = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_FORTUNE));
		loadedFortune.english = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_ENGLISH));
		loadedFortune.chinese = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_CHINESE));
		loadedFortune.pro = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_PRO));
		loadedFortune.lat = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LAT));
		loadedFortune.lon = cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LONG));

		String[] lotto = {
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO1)),
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO2)),
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO3)),
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO4)),
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO5)),
				cur.getString(cur.getColumnIndex(FortuneEntry.COLUMN_NAME_LOTTO6))
		};

		loadedFortune.lotto = lotto;


		return loadedFortune;
	}

	public ArrayList<String> listSavedFortunes() {

		SQLiteDatabase db = getReadableDatabase();
		Log.d("Loaded Database", db.getPath());
		ArrayList<String> loadlist = new ArrayList<>();
		Cursor cur = db.query(FortuneEntry.TABLE_NAME, null, null, null, null, null, null);
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			loadlist.add(cur.getString(1));
			Log.d("Retrived Fortune", cur.getString(1));
			cur.moveToNext();
		}
		db.close();
		cur.close();
		return loadlist;
	}

}
