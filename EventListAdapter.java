package com.ecomate;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class EventListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<EventsView> Items;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public EventListAdapter(Activity activity, List<EventsView> Items) {
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
			convertView = inflater.inflate(R.layout.events_list, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);

		TextView event_name = (TextView) convertView.findViewById(R.id.event_name);
		TextView loc = (TextView) convertView.findViewById(R.id.loc);
		TextView partpnt = (TextView) convertView.findViewById(R.id.partpnt);
		TextView edate = (TextView) convertView.findViewById(R.id.edate);

		TextView points = (TextView) convertView.findViewById(R.id.points);

		EventsView m = Items.get(position);

		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

		event_name.setText(m.getName());
		loc.setText(m.getLocation());
		partpnt.setText(m.getParticpant());
		edate.setText(m.getDate());
		points.setText(m.getPoints());

		return convertView;
	}

}