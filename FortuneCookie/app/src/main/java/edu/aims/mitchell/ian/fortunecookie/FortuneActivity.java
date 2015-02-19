package edu.aims.mitchell.ian.fortunecookie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FortuneActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fortune);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new FortuneSimpleFragment())
					.commit();
		}
	}
}
