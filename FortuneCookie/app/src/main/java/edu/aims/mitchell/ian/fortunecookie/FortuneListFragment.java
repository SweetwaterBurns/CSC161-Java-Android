package edu.aims.mitchell.ian.fortunecookie;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FortuneListFragment extends ListFragment {

	DatabaseHelper dbhelper;
	Fortune currentFortune;

	public FortuneListFragment() {
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			currentFortune = savedInstanceState.getParcelable("Current Fortune");
		}

		dbhelper = new DatabaseHelper(getActivity());
		ArrayList<String> fortuneList = dbhelper.listSavedFortunes();

		setListAdapter(new ArrayAdapter<String>(
				getActivity(),
				R.layout.textview_fortune_load,
				R.id.textview_fortune_load,
				fortuneList));

		setHasOptionsMenu(false);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity(), "Loading Fortune...", Toast.LENGTH_SHORT).show();

		currentFortune = dbhelper.retrieve(position);
		dbhelper.close();

		Bundle bCurrentFortune = new Bundle();
		bCurrentFortune.putParcelable("currentFortune", currentFortune);

		FortuneSimpleFragment fs = new FortuneSimpleFragment();

		fs.setArguments(bCurrentFortune);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, fs)
				.commit();

		getFragmentManager().popBackStack("Simple", getFragmentManager().POP_BACK_STACK_INCLUSIVE);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, fs)
				.commit();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable("Current Fortune", currentFortune);
		Log.d("Saved State", currentFortune.fortune);
	}
}

