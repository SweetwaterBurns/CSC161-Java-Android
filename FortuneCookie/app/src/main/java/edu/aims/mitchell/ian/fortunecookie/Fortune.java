package edu.aims.mitchell.ian.fortunecookie;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Fortune implements Parcelable {

	public String fortune;
	public String english;
	public String chinese;
	public String pro;
	public String[] lotto;


	public Fortune() {
		this.fortune = "Cause the players gonna play, play, play\n" +
				"And the haters gonna hate, hate, hate";
		this.english = "Heartbreakers gonna break, break, break";
		this.chinese = "和伪装者要去假的，假的，假的";
		this.pro = "Baby I'm just gonna...";
		this.lotto = new String[]{"play", "hate", "shake", "break", "fake", "shake"};
	}

	public Fortune(Parcel in) {
		this.fortune = in.readString();
		this.english = in.readString();
		this.chinese = in.readString();
		this.pro = in.readString();
		this.lotto = in.createStringArray();
	}

	private Void fortuneFromJson(String Json)
			throws JSONException {
		final String API_FOR = "fortune";
		final String API_MES = "message";
		final String API_LES = "lesson";
		final String API_ENG = "english";
		final String API_CHI = "chinese";
		final String API_PRO = "pronunciation";

		JSONArray cookieJson = new JSONArray(Json);
		JSONObject fortuneObj = cookieJson.getJSONObject(0).getJSONObject(API_FOR);
		JSONObject lessonObj = cookieJson.getJSONObject(0).getJSONObject(API_LES);

		this.fortune = fortuneObj.getString(API_MES);
		Log.d("Fortune: ", this.fortune);
		this.english = lessonObj.getString(API_ENG);
		Log.d("English: ", this.english);
		this.chinese = lessonObj.getString(API_CHI);
		Log.d("Chinese: ", this.chinese);
		this.pro = lessonObj.getString(API_PRO);
		Log.d("Pronunciation: ", this.pro);
		this.lotto = Lotto.random();
		return null;
	}

	public ArrayList<Fortune> asList() {
		ArrayList<Fortune> f = new ArrayList<Fortune>();
		f.add(this);
		return f;
	}

	public Void Get(String nurl) {

		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;

		String fortuneJsonStr = null;

		try {
			URL url = new URL(nurl);

			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuilder buffer = new StringBuilder();
			if (inputStream == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}

			if (buffer.length() == 0) {
				return null;
			}
			fortuneJsonStr = buffer.toString();
		} catch (IOException e) {
			Log.e("Fortune.Get(113)", "Error ", e);
			return null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e("Fortune.Get(123)", "Error closing stream", e);
				}
			}
		}
		if (fortuneJsonStr != null) {
			Log.d("FortuneJSON", fortuneJsonStr);
			try {
				this.fortuneFromJson(fortuneJsonStr);
			} catch (JSONException e) {
				Log.e("JSONParse", e.getMessage(), e);
				e.printStackTrace();
			}
		} else {
			Log.d("FortuneJSON", "Null");
		}

		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.fortune);
		dest.writeString(this.english);
		dest.writeString(this.chinese);
		dest.writeString(this.pro);
		dest.writeStringArray(this.lotto);
	}
}
