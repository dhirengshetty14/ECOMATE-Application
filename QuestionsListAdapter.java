package com.ecomate;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class QuestionsListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<QtnAnsView> Items;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public QuestionsListAdapter(Activity activity, List<QtnAnsView> Items) {
		this.activity = activity;
		this.Items = Items;
	}

	@Override
	public int getCount() {
		return Items.size();
	}

	@Override
	public Object getItem(int location) {
		return Items.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.answers_list, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView qry = (TextView) convertView.findViewById(R.id.qry);
		TextView ans = (TextView) convertView.findViewById(R.id.ans);

		QtnAnsView m = Items.get(position);


		qry.setText(m.getQuery());
		ans.setText(m.getAnswer());

		return convertView;
	}

}