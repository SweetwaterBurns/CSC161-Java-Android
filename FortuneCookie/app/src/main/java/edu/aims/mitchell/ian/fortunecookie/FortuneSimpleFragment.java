package edu.aims.mitchell.ian.fortunecookie;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class FortuneSimpleFragment extends Fragment implements Shaker.Callback {

	Intent fortuneShare = new Intent();
	ShareActionProvider mShareActionProvider;// = new ShareActionProvider(getActivity());
	ArrayAdapter<String> FortuneAdapter;
	String[] currentFortune;// = {"", "", "", "", "", "", "", "", "", ""};
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
		inflater.inflate(R.menu.menu_simple, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
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

		if (id == R.id.action_fortune_detail) {
			Bundle bCurrentFortune = new Bundle();
			bCurrentFortune.putStringArray("currentFortune", currentFortune);
			FortuneDetailFragment fd = new FortuneDetailFragment();

			fd.setArguments(bCurrentFortune);

			getFragmentManager().beginTransaction()
					.replace(R.id.container, fd)
					.commit();
		}

		//if (id == R.id.menu_item_share){}


		return super.onOptionsItemSelected(item);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		Bundle bCurrentFortune = getArguments();
		new Shaker(getActivity(), 2.0d, 750, this);

		ArrayList<String> fortune = new ArrayList<>(Arrays.asList(""));
		View rootView = inflater.inflate(R.layout.fragment_simple, container, false);

		FortuneAdapter =
				new ArrayAdapter<>(
						getActivity(),
						R.layout.text_simple,
						R.id.text_simple_textview,
						fortune);

		ListView fortuneLinearView = (ListView) rootView.findViewById(R.id.fortune_simple_view);

		fortuneLinearView.setAdapter(FortuneAdapter);

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

		FortuneAdapter.clear();
		ArrayList<String> fortune = new ArrayList<>(Arrays.asList(currentFortune[0]));
		FortuneAdapter.addAll(fortune);

		fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune[0]);
		setShare(fortuneShare);
	}

	@Override
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


	private class FortuneAsyncTask extends android.os.AsyncTask<Void, Void, String[]> {

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