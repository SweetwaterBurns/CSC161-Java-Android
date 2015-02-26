package edu.aims.mitchell.ian.fortunecookie;

import android.app.Activity;
import android.os.Bundle;

public class FortuneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fortune);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new FortuneSimpleFragment())
					.commit();
		}
	}
}
