package edu.aims.mitchell.ian.fortunecookie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ian on 1/20/2015.
 */
public class FortuneFragment extends Fragment {


    ArrayAdapter<String> LottoAdapter;
    ArrayAdapter<String> FortuneAdapter;
    ArrayAdapter<String> LangEngAdapter;
    ArrayAdapter<String> LangChiAdapter;
    ArrayAdapter<String> LangProAdapter;

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

        final Button lotto_button = (Button) rootView.findViewById(R.id.lotto_button);
        lotto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FortuneAsyncTask fortuneTask = new FortuneAsyncTask();
                fortuneTask.execute();
            }
        });


        return rootView;
    }

    public class FortuneAsyncTask extends AsyncTask<Void, Void, Void> {

        String[] f = new String[1];

        @Override
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String fortuneJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(getString(R.string.url_fortune));// + "adbd");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                fortuneJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FortuneFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FortuneFragment", "Error closing stream", e);
                    }
                }
            }
            //if (fortuneJsonStr != null) {
            f[0] = fortuneJsonStr;
            Log.d("FortuneJSON", f[0]);
            /*} /*else {
                f[0] = getString(R.string.fortune_text);
                Log.d("FortuneJSON", "Null");
            }*/
            return null;

        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                LottoAdapter.clear();
                ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
                LottoAdapter.addAll(lottoNumbers);

                FortuneAdapter.clear();
                ArrayList<String> fortune = new ArrayList<String>(Arrays.asList(f));
                FortuneAdapter.addAll(fortune);
            }
        }
    }
}

