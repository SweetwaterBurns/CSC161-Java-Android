package edu.aims.mitchell.ian.fortunecookie;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FortuneAdapter extends ArrayAdapter<Fortune> {

	public FortuneAdapter(Context ctx, int resource, int textViewResourceId, ArrayList<Fortune> fortune) {

		super(ctx, resource, textViewResourceId,fortune);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		Fortune data = (Fortune) getItem(position);
		Log.d("Adapter Fortune: ", data.fortune);
		Log.d("Adapter English: ", data.english);
		Log.d("Adapter Chinese: ", data.chinese);
		Log.d("Adapter Pronunciation: ", data.pro);


		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail, parent, false);
		}

		ListView lvFortune = (ListView) convertView.findViewById(R.id.fortune_detail_view);
		ListView lvEnglish = (ListView) convertView.findViewById(R.id.lang_eng_view);
		ListView lvChinese = (ListView) convertView.findViewById(R.id.lang_chi_view);
		ListView lvPro = (ListView) convertView.findViewById(R.id.lang_pro_view);


		/*tvFortune.setText(data.fortune);
		tvEnglish.setText(data.english);
		tvChinese.setText(data.chinese);
		tvPro.setText(data.pro);
*/
		return convertView;
	}
}
