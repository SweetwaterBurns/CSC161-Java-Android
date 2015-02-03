package edu.aims.mitchell.ian.fortunecookie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

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
import java.util.Arrays;
import java.util.Random;


/**
 * Created by Ian on 1/20/2015.
 */
public class FortuneFragment extends Fragment {

	ArrayAdapter<String> LottoAdapter;
	ArrayAdapter<String> FortuneAdapter;
	ArrayAdapter<String> LangEngAdapter;
	ArrayAdapter<String> LangChiAdapter;
	ArrayAdapter<String> LangProAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		ArrayList<String> fortune = new ArrayList<String>(Arrays.asList(getString(R.string.fortune_text)));
		ArrayList<String> langEng = new ArrayList<String>(Arrays.asList(getString(R.string.english_text)));
		ArrayList<String> langChi = new ArrayList<String>(Arrays.asList(getString(R.string.chinese_text)));
		ArrayList<String> langPro = new ArrayList<String>(Arrays.asList(getString(R.string.pro_text)));
		ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		FortuneAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.text,
						R.id.text_textview,
						fortune);

		LangEngAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.text,
						R.id.text_textview,
						langEng);
		LangChiAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.text,
						R.id.text_textview,
						langChi);
		LangProAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.text,
						R.id.text_textview,
						langPro);
		LottoAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.lotto,
						R.id.lotto_textview,
						lottoNumbers);

		ListView fortuneLinearView = (ListView) rootView.findViewById(R.id.fortune_view);
		ListView langEngLinearView = (ListView) rootView.findViewById(R.id.lang_eng_view);
		ListView langChiLinearView = (ListView) rootView.findViewById(R.id.lang_chi_view);
		ListView langProLinearView = (ListView) rootView.findViewById(R.id.lang_pro_view);
		GridView lottoGridView = (GridView) rootView.findViewById(R.id.lotto_numbers);

		fortuneLinearView.setAdapter(FortuneAdapter);
		langEngLinearView.setAdapter(LangEngAdapter);
		langChiLinearView.setAdapter(LangChiAdapter);
		langProLinearView.setAdapter(LangProAdapter);
		lottoGridView.setAdapter(LottoAdapter);

		final Button lotto_button = (Button) rootView.findViewById(R.id.lotto_button);
		lotto_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
				fortuneTask.execute();
			}
		});


		return rootView;
	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, String[]> {

		String hex;
		Random rnd = new Random();

		private String[] fortuneFromJson(String Json)
				throws JSONException {
			//String[] sort;

			final String API_FOR = "fortune";
			final String API_MES = "message";
			final String API_LOT = "lotto";
			final String API_NUM = "numbers";
			final String API_LES = "lesson";
			final String API_ENG = "english";
			final String API_CHI = "chinese";
			final String API_PRO = "pronunciation";

			JSONArray cookieJson = new JSONArray(Json);
			JSONObject fortuneObj = cookieJson.getJSONObject(0).getJSONObject(API_FOR);
			JSONObject lessonObj = cookieJson.getJSONObject(0).getJSONObject(API_LES);
			JSONObject lottoObj = cookieJson.getJSONObject(0).getJSONObject(API_LOT);

			String fortune = fortuneObj.getString(API_MES);
			Log.d("Fortune: ", fortune);
			String english = lessonObj.getString(API_ENG);
			Log.d("English: ", english);
			String chinese = lessonObj.getString(API_CHI);
			Log.d("Chinese: ", chinese);
			String pro = lessonObj.getString(API_PRO);
			Log.d("Pronunciation: ", pro);

			String[] numbers = new String[6];

			for (int i = 0; i < 6; i++) {
				numbers[i] = Integer.toString(lottoObj.getJSONArray(API_NUM).getInt(i));
				Log.d("Number " + i + ": ", numbers[i]);
			}


			String[] sort = {fortune, english, chinese, pro, numbers[0], numbers[1], numbers[2],
					numbers[3], numbers[4], numbers[5]};

			return sort;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			// These two need to be declared outside the try/catch
			// so that they can be closed in the finally block.

			//hex = Integer.toHexString(rnd.nextInt(543) + 43828);

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			// Will contain the raw JSON response as a string.
			String fortuneJsonStr = null;

			try {
				URL url = new URL(getString(R.string.url_fortune));// + hex);

				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					// Nothing to do.
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					// Stream was empty.  No point in parsing.
					return null;
				}
				fortuneJsonStr = buffer.toString();
			} catch (IOException e) {
				Log.e("FortuneFragment", "Error ", e);
				// If the code didn't successfully get the weather data, there's no point in attemping
				// to parse it.
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e("FortuneFragment", "Error closing stream", e);
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


		protected void onPostExecute(String[] result) {

			if (result != null) {
				int[] numbers = new int[6];

				FortuneAdapter.clear();
				ArrayList<String> fortune = new ArrayList<String>(Arrays.asList(result[0]));
				FortuneAdapter.addAll(fortune);

				LangEngAdapter.clear();
				ArrayList<String> english = new ArrayList<>(Arrays.asList(result[1]));
				LangEngAdapter.addAll(english);

				LangChiAdapter.clear();
				ArrayList<String> chinese = new ArrayList<>(Arrays.asList(result[2]));
				LangChiAdapter.addAll(chinese);

				LangProAdapter.clear();
				ArrayList<String> pro = new ArrayList<>(Arrays.asList(result[3]));
				LangProAdapter.addAll(pro);

				for (int i = 0; i < 6; i++) {
					numbers[i] = Integer.valueOf(result[i + 4]);
				}

				LottoAdapter.clear();
				ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
				LottoAdapter.addAll(lottoNumbers);

			}

		}
	}

}


