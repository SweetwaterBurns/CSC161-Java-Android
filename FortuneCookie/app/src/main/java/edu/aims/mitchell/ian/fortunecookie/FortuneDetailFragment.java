package edu.aims.mitchell.ian.fortunecookie;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class FortuneDetailFragment extends Fragment implements Shaker.Callback {

	ArrayAdapter<String> LottoAdapter;
	ArrayAdapter<String> FortuneAdapter;
	ArrayAdapter<String> LangEngAdapter;
	ArrayAdapter<String> LangChiAdapter;
	ArrayAdapter<String> LangProAdapter;
	String[] currentFortune;// = {"", "", "", "", "", "", "", "", "", ""};
	ShareActionProvider mShareActionProvider;// = new ShareActionProvider(getActivity());
	Intent fortuneShare = new Intent();
	Boolean readPassedData = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			currentFortune = savedInstanceState.getStringArray("Current Fortune");
			Log.d("Restored State", currentFortune[0]);
		}

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_detail, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		//Intent fortuneShare = new Intent();
		fortuneShare.setAction(Intent.ACTION_SEND);
		fortuneShare.setType("text/plain");
		if (currentFortune != null) {
			fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune[0]);
		}
		setShare(fortuneShare);
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

		if (id == R.id.action_fortune_simple) {
			Bundle bCurrentFortune = new Bundle();
			bCurrentFortune.putStringArray("currentFortune", currentFortune);
			FortuneSimpleFragment fs = new FortuneSimpleFragment();

			fs.setArguments(bCurrentFortune);

			getFragmentManager().beginTransaction()
					.replace(R.id.container, fs)
					.commit();
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		Bundle bCurrentFortune = getArguments();
		new Shaker(getActivity(), 2.0d, 750, this);

		ArrayList<String> fortune = new ArrayList<>(Arrays.asList(""));
		ArrayList<String> langEng = new ArrayList<>(Arrays.asList(""));
		ArrayList<String> langChi = new ArrayList<>(Arrays.asList(""));
		ArrayList<String> langPro = new ArrayList<>(Arrays.asList(""));
		ArrayList<String> lottoNumbers = new ArrayList<>(Arrays.asList(""));

		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

		FortuneAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_detail,
						R.id.text_detail_textview,
						fortune);

		LangEngAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_detail,
						R.id.text_detail_textview,
						langEng);
		LangChiAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_detail,
						R.id.text_detail_textview,
						langChi);
		LangProAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_detail,
						R.id.text_detail_textview,
						langPro);
		LottoAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_lotto,
						R.id.lotto_textview,
						lottoNumbers);

		ListView fortuneLinearView = (ListView) rootView.findViewById(R.id.fortune_detail_view);
		ListView langEngLinearView = (ListView) rootView.findViewById(R.id.lang_eng_view);
		ListView langChiLinearView = (ListView) rootView.findViewById(R.id.lang_chi_view);
		ListView langProLinearView = (ListView) rootView.findViewById(R.id.lang_pro_view);
		GridView lottoGridView = (GridView) rootView.findViewById(R.id.lotto_numbers);

		fortuneLinearView.setAdapter(FortuneAdapter);
		langEngLinearView.setAdapter(LangEngAdapter);
		langChiLinearView.setAdapter(LangChiAdapter);
		langProLinearView.setAdapter(LangProAdapter);
		lottoGridView.setAdapter(LottoAdapter);

		if (currentFortune != null) {
			Refresh();
		} else if (bCurrentFortune != null && readPassedData == false) {
			readPassedData = true;
			currentFortune = bCurrentFortune.getStringArray("currentFortune");
			for (int i = 0; i < 10; i++) {
				Log.d("Passed Fortune " + i + ": ", currentFortune[i]);
			}
			Refresh();
		} else if (currentFortune == null) {
			FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
			fortuneTask.execute();
		}
		return rootView;
	}

	private void Refresh() {

		String[] numbers = new String[6];

		System.arraycopy(currentFortune, 4, numbers, 0, 6);

		FortuneAdapter.clear();
		ArrayList<String> fortune = new ArrayList<>(Arrays.asList(currentFortune[0]));
		FortuneAdapter.addAll(fortune);

		LangEngAdapter.clear();
		ArrayList<String> english = new ArrayList<>(Arrays.asList(currentFortune[1]));
		LangEngAdapter.addAll(english);

		LangChiAdapter.clear();
		ArrayList<String> chinese = new ArrayList<>(Arrays.asList(currentFortune[2]));
		LangChiAdapter.addAll(chinese);

		LangProAdapter.clear();
		ArrayList<String> pro = new ArrayList<>(Arrays.asList(currentFortune[3]));
		LangProAdapter.addAll(pro);

		LottoAdapter.clear();
		ArrayList<String> lottoNumbers = new ArrayList<>(Arrays.asList(numbers));
		LottoAdapter.addAll(lottoNumbers);

		fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune[0]);
		setShare(fortuneShare);
	}

	public void shakingStarted() {
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(getActivity(), R.string.toast_shake, duration).show();
		FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
		fortuneTask.execute();
	}

	@Override
	public void shakingStopped() {

	}

	private void setShare(Intent fortuneShare) {
		if (fortuneShare != null && mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(fortuneShare);
		}

	}


	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putStringArray("Current Fortune", currentFortune);
		Log.d("Saved State", currentFortune[0]);

	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			return UpdateFortune.Get(getString(R.string.url_fortune));
		}


		protected void onPostExecute(String[] result) {

			if (result != null) {
				currentFortune = result;
				Refresh();
			}
		}
	}
}
