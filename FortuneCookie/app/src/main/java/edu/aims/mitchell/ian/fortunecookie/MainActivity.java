package edu.aims.mitchell.ian.fortunecookie;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

    static ArrayAdapter<String> LottoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FortuneFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FortuneFragment extends Fragment {

        public FortuneFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
          /*  Fortune ffortune = new Fortune(this.getActivity());
            Lang llang = new Lang(this.getActivity());


            ArrayList<String> fortune = new ArrayList<>(Arrays.asList(ffortune.random()));
            ArrayList<String> lang = new ArrayList<>(Arrays.asList(llang.random()));
*/

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] numbers = Lotto.random();
            ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(numbers));

            LottoAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.lotto,
                            R.id.lotto_textview,
                            lottoNumbers);

            GridView lottoGridView = (GridView) rootView.findViewById(R.id.lotto_numbers);
            lottoGridView.setAdapter(LottoAdapter);


            return rootView;
        }
    }

    public void onClick(View view){
        String[] numbers = Lotto.random();
        LottoAdapter.clear();
        ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(numbers));
        LottoAdapter.addAll(lottoNumbers);
    }
}
