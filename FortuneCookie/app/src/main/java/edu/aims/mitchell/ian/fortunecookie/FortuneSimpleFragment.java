package edu.aims.mitchell.ian.fortunecookie;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;

public class FortuneSimpleFragment extends Fragment implements Shaker.Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	Intent fortuneShare = new Intent();
	ShareActionProvider mShareActionProvider;// = new ShareActionProvider(getActivity());
	ArrayAdapter<String> FortuneAdapter;
	Fortune currentFortune = new Fortune();
	Shaker shaker;
	DatabaseHelper dbhelper;
	GoogleApiClient mGoogleApiClient;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			currentFortune = savedInstanceState.getParcelable("Current Fortune");
			Log.d("Restored State", currentFortune.fortune);
		}

		setHasOptionsMenu(true);
		buildGoogleApiClient(getActivity());


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
		}

		if (id == R.id.action_fortune_detail) {
			Bundle bCurrentFortune = new Bundle();
			bCurrentFortune.putParcelable("currentFortune", currentFortune);
			FortuneDetailFragment fd = new FortuneDetailFragment();

			fd.setArguments(bCurrentFortune);

			getFragmentManager().beginTransaction()
					//.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
					.replace(R.id.container, fd)
					.addToBackStack("Simple")
					.commit();
		}


		if (id == R.id.menu_item_save) {
			dbhelper.add(currentFortune);
			Toast.makeText(getActivity(), getString(R.string.fortune_saved), Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		Bundle bCurrentFortune = getArguments();
		shaker = new Shaker(getActivity(), 2.0d, 750, this);
		dbhelper = new DatabaseHelper(getActivity());

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

		if (bCurrentFortune != null) {
			currentFortune = bCurrentFortune.getParcelable("currentFortune");
			Log.d("Passed Fortune: ", currentFortune.fortune);
			Refresh();
		} else if (currentFortune.fortune == "") {
			FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
			fortuneTask.execute();
		} else {
			Refresh();
		}
		return rootView;
	}

	private void Refresh() {

		FortuneAdapter.clear();
		ArrayList<String> fortune = new ArrayList<>(Arrays.asList(currentFortune.fortune));
		FortuneAdapter.addAll(fortune);

		fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune.fortune);
		setShare(fortuneShare);
	}

	@Override
	public void shakingStarted() {
		Toast.makeText(getActivity(), R.string.toast_shake, Toast.LENGTH_SHORT).show();
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
		Log.d("In:", "onPause");
		super.onPause();
		shaker.close();
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onStop() {
		Log.d("In:", "onStop");
		super.onStop();
		shaker.close();
		mGoogleApiClient.disconnect();
	}

	@Override
	public void onStart() {
		Log.d("In:", "onStart");
		super.onStart();
		shaker = new Shaker(getActivity(), 2.0d, 750, this);
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(Bundle bundle) {

		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			currentFortune.lat = String.valueOf(mLastLocation.getLatitude());
			currentFortune.lon = String.valueOf(mLastLocation.getLongitude());
			Log.d("Bundle Latitude: ", currentFortune.lat);
			Log.d("Bundle Longitude: ", currentFortune.lon);
		} else if (mLastLocation == null) {
			Log.d("mLastLocation", " = null");
		}
		Log.d("FortuneFragmentSimple.", "onConnected");
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d("FortuneFragmentSimple.", "onConnectionSuspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d("FortuneFragmentSimple", " Connection Failed");
	}

	protected synchronized void buildGoogleApiClient(Context ctx) {
		mGoogleApiClient = new GoogleApiClient.Builder(ctx)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		Log.d("buildGoogleApiClient", "Client Created");
	}

	private class FortuneAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			currentFortune.Get(getString(R.string.url_fortune));
			return null;
		}

		protected void onPostExecute(Void v) {
			if (currentFortune.fortune != "") {
				Refresh();
			}
		}
	}
}

