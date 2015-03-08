package edu.aims.mitchell.ian.fortunecookie;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class FragmentFortuneLoad extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new fragment_fortune_load())
					.commit();
		}
	}

	public static class fragment_fortune_load extends Fragment {

		public fragment_fortune_load() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_fortune_load, container, false);

			DatabaseHelper dbhelper = new DatabaseHelper(getActivity());

			ArrayList<String> fortuneList = dbhelper.listSavedFortunes();

			ArrayAdapter<String> loadAdapter =
					new ArrayAdapter<>(
							getActivity(),
							R.layout.textview_fortune_load,
							R.id.textview_fortune_load,
							fortuneList);

			ListView loadListview = (ListView) rootView.findViewById(R.id.listview_load_fortune);
			loadListview.setAdapter(loadAdapter);

			return rootView;
		}
	}
}
