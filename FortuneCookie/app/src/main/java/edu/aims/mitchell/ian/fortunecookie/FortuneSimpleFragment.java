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

public class FortuneSimpleFragment extends Fragment {

	ArrayAdapter<String> FortuneAdapter;


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

		View rootView = inflater.inflate(R.layout.fragment_simple, container, false);

		FortuneAdapter =
				new ArrayAdapter<String>(
						getActivity(),
						R.layout.text_simple,
						R.id.text_simple_textview,
						fortune);


		ListView fortuneLinearView = (ListView) rootView.findViewById(R.id.fortune_simple_view);

		fortuneLinearView.setAdapter(FortuneAdapter);


		FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
		fortuneTask.execute();

		return rootView;
	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, String[]> {

		private String[] fortuneFromJson(String Json)
				throws JSONException {
			final String API_FOR = "fortune";
			final String API_MES = "message";

			JSONArray cookieJson = new JSONArray(Json);
			JSONObject fortuneObj = cookieJson.getJSONObject(0).getJSONObject(API_FOR);

			String fortune = fortuneObj.getString(API_MES);
			Log.d("Fortune: ", fortune);

			String[] sort = {fortune};

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
				Log.e("FortuneSimpleFragment", "Error ", e);
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e("FortuneSimpleFragment", "Error closing stream", e);
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
			}

		}
	}

}
