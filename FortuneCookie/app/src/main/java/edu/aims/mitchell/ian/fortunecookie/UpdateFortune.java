package edu.aims.mitchell.ian.fortunecookie;


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

public class UpdateFortune {


	private static String[] fortuneFromJson(String Json)
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

		String fortune = fortuneObj.getString(API_MES);
		Log.d("Fortune: ", fortune);
		String english = lessonObj.getString(API_ENG);
		Log.d("English: ", english);
		String chinese = lessonObj.getString(API_CHI);
		Log.d("Chinese: ", chinese);
		String pro = lessonObj.getString(API_PRO);
		Log.d("Pronunciation: ", pro);
		String[] lotto = Lotto.random();

		return new String[]{fortune, english, chinese, pro, lotto[0], lotto[1], lotto[2], lotto[3], lotto[4], lotto[5]};
	}

	public static String[] Get(String nurl) {

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
			Log.e("UpdateFortune.Get(79)", "Error ", e);
			return null;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e("UpdateFortune.Get(89)", "Error closing stream", e);
				}
			}
		}
		if (fortuneJsonStr != null) {
			Log.d("FortuneJSON", fortuneJsonStr);
		} else {
			Log.d("FortuneJSON", "Null");
		}
		try {
			return fortuneFromJson(fortuneJsonStr);
		} catch (JSONException e) {
			Log.e("JSONParse", e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
}
