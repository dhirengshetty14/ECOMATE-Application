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

public class AcceptedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<RequestsView> Items;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public AcceptedListAdapter(Activity activity, List<RequestsView> Items) {
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
			convertView = inflater.inflate(R.layout.acepted_request_list, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();


		TextView ename = (TextView) convertView.findViewById(R.id.ename);
		TextView loc = (TextView) convertView.findViewById(R.id.loc);
		TextView edate = (TextView) convertView.findViewById(R.id.edate);

		RequestsView m = Items.get(position);

		loc.setText(m.getLocation());
		ename.setText(m.getEventname());
		edate.setText(m.getDate());

		return convertView;
	}

}