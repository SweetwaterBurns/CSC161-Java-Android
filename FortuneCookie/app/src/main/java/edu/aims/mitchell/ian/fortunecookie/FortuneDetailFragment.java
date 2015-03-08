package edu.aims.mitchell.ian.fortunecookie;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;

public class FortuneDetailFragment extends Fragment implements Shaker.Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	ArrayAdapter<String> LottoAdapter;
	FortuneAdapter fortuneAdapter;
	Fortune currentFortune = new Fortune();
	ShareActionProvider mShareActionProvider;
	Intent fortuneShare = new Intent();
	Shaker shaker;
	DatabaseHelper dbhelper;
	GoogleApiClient mGoogleApiClient;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			currentFortune = savedInstanceState.getParcelable("Current Fortune");
		}

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//Clear Menu items from previous Fragments
		menu.clear();
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
		int id = item.getItemId();

		if (id != R.id.menu_item_save && id!=R.id.action_new_fortune) {
			if (shaker != null) {
				shaker.close();
				shaker = null;
			}
			if (mGoogleApiClient != null) {
				mGoogleApiClient.disconnect();
				mGoogleApiClient = null;
			}
		}

		Bundle bCurrentFortune = new Bundle();
		bCurrentFortune.putParcelable("currentFortune", currentFortune);

		if (id == R.id.menu_item_load) {

			FortuneListFragment fl = new FortuneListFragment();

			getFragmentManager().beginTransaction()
					//.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
					.replace(R.id.container, fl)
					.commit();
		}

		if (id == R.id.action_new_fortune) {
			FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
			fortuneTask.execute();
		}

		if (id == R.id.action_fortune_simple) {

			FortuneSimpleFragment fs = new FortuneSimpleFragment();

			fs.setArguments(bCurrentFortune);

			getFragmentManager().popBackStack("Simple", getFragmentManager().POP_BACK_STACK_INCLUSIVE);

			getFragmentManager().beginTransaction()
					.replace(R.id.container, fs)
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

		if (bCurrentFortune != null) {
			currentFortune = bCurrentFortune.getParcelable("currentFortune");
			Log.d("Passed Fortune: ", currentFortune.fortune);
			Refresh();
		} else {
			Refresh();
		}

		return rootView;
	}

	private void Refresh() {

		Log.d("Fortune to Display: ", currentFortune.fortune);

		LottoAdapter.clear();
		ArrayList<String> lottoNumbers = new ArrayList<>(Arrays.asList(currentFortune.lotto));
		LottoAdapter.addAll(lottoNumbers);

		fortuneAdapter.clear();
		//ArrayList<Fortune> fortune = new ArrayList<>(currentFortune.asList());
		fortuneAdapter.addAll(currentFortune.asList());

		fortuneShare.putExtra(Intent.EXTRA_TEXT, "My Fortune:\n" + currentFortune.fortune);
		setShare(fortuneShare);
	}

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
		super.onPause();
		if (shaker != null) {
			shaker.close();
			shaker = null;
		}
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
			mGoogleApiClient = null;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (shaker != null) {
			shaker.close();
			shaker = null;
		}
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
			mGoogleApiClient = null;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (shaker == null) {
			shaker = new Shaker(getActivity(), 2.0d, 750, this);
		}
		if (mGoogleApiClient == null) {
			buildGoogleApiClient(getActivity());
		}
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
		Log.d("FortuneFragmentDetail.", "onConnected");
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d("FortuneFragmentDetail.", "onConnectionSuspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d("FortuneFragmentDetail", " Connection Failed");
	}

	protected synchronized void buildGoogleApiClient(Context ctx) {
		mGoogleApiClient = new GoogleApiClient.Builder(ctx)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		Log.d("buildGoogleApiClient", "Client Created");
	}

	public class FortuneAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			currentFortune.Get(getString(R.string.url_fortune));
			return null;
		}


		protected void onPostExecute(Void V) {
			if (currentFortune.fortune != "") {
				Refresh();
			}
		}
	}


}
