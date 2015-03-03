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
	FortuneAdapter fortuneAdapter;
	Fortune currentFortune = new Fortune();
	ShareActionProvider mShareActionProvider;
	Intent fortuneShare = new Intent();
	Shaker shaker;
	DatabaseHelper dbhelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			currentFortune = savedInstanceState.getParcelable("Current Fortune");
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
		if (currentFortune.fortune != "") {
			fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune.fortune);
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
			bCurrentFortune.putParcelable("currentFortune", currentFortune);
			FortuneSimpleFragment fs = new FortuneSimpleFragment();

			fs.setArguments(bCurrentFortune);

			getFragmentManager().popBackStackImmediate("Simple", getFragmentManager().POP_BACK_STACK_INCLUSIVE);
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		Bundle bCurrentFortune = getArguments();
		shaker = new Shaker(getActivity(), 2.0d, 750, this);
		dbhelper = new DatabaseHelper(getActivity());

		ArrayList<Fortune> fortune = new ArrayList<>(new Fortune().asList());
		ArrayList<String> lottoNumbers = new ArrayList<>(Arrays.asList(""));

		View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


		LottoAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_lotto,
						R.id.lotto_textview,
						lottoNumbers);

		fortuneAdapter =
				new FortuneAdapter(
						getActivity(),
						fortune);

		ListView fortuneListView = (ListView) rootView.findViewById(R.id.listview_fortune_detail);
		GridView lottoGridView = (GridView) rootView.findViewById(R.id.lotto_numbers);

		lottoGridView.setAdapter(LottoAdapter);
		fortuneListView.setAdapter(fortuneAdapter);

		Refresh();

		if (bCurrentFortune != null) {
			currentFortune = bCurrentFortune.getParcelable("currentFortune");
			Log.d("Passed Fortune: ", currentFortune.fortune);
			Refresh();
		} else if (currentFortune.fortune == "") {
			FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
			fortuneTask.execute();
		}

		return rootView;
	}

	private void Refresh() {

		Log.d("Fortune to Display: ", currentFortune.fortune);

		LottoAdapter.clear();
		ArrayList<String> lottoNumbers = new ArrayList<>(Arrays.asList(currentFortune.lotto));
		LottoAdapter.addAll(lottoNumbers);

		fortuneAdapter.clear();
		ArrayList<Fortune> fortune = new ArrayList<>(currentFortune.asList());
		fortuneAdapter.addAll(fortune);

		fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune.fortune);
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
		savedInstanceState.putParcelable("Current Fortune", currentFortune);
		Log.d("Saved State", currentFortune.fortune);
	}

	@Override
	public void onPause() {
		super.onPause();
		shaker.close();
	}

	@Override
	public void onStop() {
		super.onStop();
		shaker.close();
	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			currentFortune.Get(getString(R.string.url_fortune));
			return null;
		}


		protected void onPostExecute(Void V) {
			if (currentFortune.fortune != "") {
				dbhelper.add(currentFortune);
				Refresh();
			}
		}
	}
}
