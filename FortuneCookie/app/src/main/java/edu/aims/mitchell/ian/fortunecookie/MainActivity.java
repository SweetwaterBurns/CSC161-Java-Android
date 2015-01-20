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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ActionBarActivity {

    static ArrayAdapter<String> LottoAdapter;
    static ArrayAdapter<String> FortuneAdapter;
    static ArrayAdapter<String> LangEngAdapter;
    static ArrayAdapter<String> LangChiAdapter;
    static ArrayAdapter<String> LangProAdapter;

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

            ArrayList<String> fortune = new ArrayList<String>(Arrays.asList(getString(R.string.fortune_text)));
            ArrayList<String> langEng = new ArrayList<String>(Arrays.asList(getString(R.string.english_text)));
            ArrayList<String> langChi = new ArrayList<String>(Arrays.asList(getString(R.string.chinese_text)));
            ArrayList<String> langPro = new ArrayList<String>(Arrays.asList(getString(R.string.pro_text)));
            ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            FortuneAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.text,
                            R.id.text_textview,
                            fortune);

            LangEngAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.text,
                            R.id.text_textview,
                            langEng);
            LangChiAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.text,
                            R.id.text_textview,
                            langChi);
            LangProAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.text,
                            R.id.text_textview,
                            langPro);
            LottoAdapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.lotto,
                            R.id.lotto_textview,
                            lottoNumbers);

            ListView fortuneLinearView = (ListView) rootView.findViewById(R.id.fortune_view);
            ListView langEngLinearView = (ListView) rootView.findViewById(R.id.lang_eng_view);
            ListView langChiLinearView = (ListView) rootView.findViewById(R.id.lang_chi_view);
            ListView langProLinearView = (ListView) rootView.findViewById(R.id.lang_pro_view);
            GridView lottoGridView = (GridView) rootView.findViewById(R.id.lotto_numbers);

            fortuneLinearView.setAdapter(FortuneAdapter);
            langEngLinearView.setAdapter(LangEngAdapter);
            langChiLinearView.setAdapter(LangChiAdapter);
            langProLinearView.setAdapter(LangProAdapter);
            lottoGridView.setAdapter(LottoAdapter);


            return rootView;
        }
    }

    public void onClick(View view){
        LottoAdapter.clear();
        ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
        LottoAdapter.addAll(lottoNumbers);
    }
}
