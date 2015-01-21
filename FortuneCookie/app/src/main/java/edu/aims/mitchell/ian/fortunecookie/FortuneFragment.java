package edu.aims.mitchell.ian.fortunecookie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
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
                LottoAdapter.clear();
                ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
                LottoAdapter.addAll(lottoNumbers);
            }
        });

        return rootView;
    }

    public void onClick(View view){
        LottoAdapter.clear();
        ArrayList<String> lottoNumbers = new ArrayList<String>(Arrays.asList(Lotto.random()));
        LottoAdapter.addAll(lottoNumbers);
    }
}
