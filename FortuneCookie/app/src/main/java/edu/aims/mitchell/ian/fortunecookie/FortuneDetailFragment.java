package edu.aims.mitchell.ian.fortunecookie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class FortuneDetailFragment extends Fragment {

	ArrayAdapter<String> LottoAdapter;
	ArrayAdapter<String> FortuneAdapter;
	ArrayAdapter<String> LangEngAdapter;
	ArrayAdapter<String> LangChiAdapter;
	ArrayAdapter<String> LangProAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_new_fortune) {
			FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
			fortuneTask.execute();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		ArrayList<String> fortune = new ArrayList<String>(Arrays.asList(""));
		ArrayList<String> langEng = new ArrayList<String>(Arrays.asList(""));
		ArrayList<String> langChi = new ArrayList<String>(Arrays.asList(""));
		ArrayList<String> langPro = new ArrayList<String>(Arrays.asList(""));
		ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(""));

		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

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


		FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
		fortuneTask.execute();

		return rootView;
	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, String[]> {

		private String[] fortuneFromJson(String Json)
				throws JSONException {
			final String API_FOR = "fortune";
			final String API_MES = "message";
			final String API_LOT = "lotto";
			//final String API_NUM = "numbers";
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

/*I don't like that the API will send doubles of numbers... Not really useful for picking Lotto Numbers.
 *Kept the code I used in so you could see how I parsed it...
 */

/* 			String[] numbers = new String[6];

			for (int i = 0; i < 6; i++) {
				numbers[i] = Integer.toString(lottoObj.getJSONArray(API_NUM).getInt(i));
				Log.d("Number " + i + ": ", numbers[i]);
			}
*/

/*No need to pass the numbers back as I'm not using them.
 */
			String[] sort = {fortune, english, chinese, pro};//, numbers[0], numbers[1], numbers[2],numbers[3], numbers[4], numbers[5]};

			return sort;
		}

		@Override
		protected String[] doInBackground(Void... params) {

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			String fortuneJsonStr = null;

			try {
				URL url = new URL(getString(R.string.url_fortune));

				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					return null;
				}
				fortuneJsonStr = buffer.toString();
			} catch (IOException e) {
				Log.e("FortuneDetailFragment", "Error ", e);
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e("FortuneDetailFragment", "Error closing stream", e);
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

/*Would use this Array for displaying the Lotto Numbers in place of Lotto.random() if I liked the values it returned.
 */

/*				for (int i = 0; i < 6; i++) {
					numbers[i] = Integer.valueOf(result[i + 4]);
				}
*/
				LottoAdapter.clear();
				ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
				LottoAdapter.addAll(lottoNumbers);

			}

		}
	}

}
