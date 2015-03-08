package edu.aims.mitchell.ian.fortunecookie;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FortuneListFragment extends ListFragment {

	public FortuneListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHelper dbhelper = new DatabaseHelper(getActivity());

		ArrayList<String> fortuneList = dbhelper.listSavedFortunes();

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.textview_fortune_load, R.id.textview_fortune_load, fortuneList));
	}
	
	public void onListItemClick(ListView l, View v, int position, long id){

	}
}

