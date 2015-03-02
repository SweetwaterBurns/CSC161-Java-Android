package edu.aims.mitchell.ian.fortunecookie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FortuneAdapter extends ArrayAdapter<Fortune> {

	public FortuneAdapter(Context ctx, ArrayList<Fortune> fortune) {
		super(ctx, 0, fortune);

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Fortune data = getItem(position);
		Log.d("Adapter Fortune: ", data.fortune);
		Log.d("Adapter English: ", data.english);
		Log.d("Adapter Chinese: ", data.chinese);
		Log.d("Adapter Pronunciation: ", data.pro);


		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_detail, parent, false);
		}

		TextView tvFortune = (TextView) convertView.findViewById(R.id.fortune_detail_view);
		TextView tvEnglish = (TextView) convertView.findViewById(R.id.lang_eng_view);
		TextView tvChinese = (TextView) convertView.findViewById(R.id.lang_chi_view);
		TextView tvPro = (TextView) convertView.findViewById(R.id.lang_pro_view);


		tvFortune.setText(data.fortune);
		tvEnglish.setText(data.english);
		tvChinese.setText(data.chinese);
		tvPro.setText(data.pro);

		return convertView;
	}
}
