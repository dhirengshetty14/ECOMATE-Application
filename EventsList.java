package com.ecomate;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class EventsList extends AppCompatActivity implements AdapterView.OnItemClickListener {
	private ProgressDialog pDialog;
	String event_sno;
	private List<EventsView> cList = new ArrayList<EventsView>();
	private ListView listView;
	PostServer server = new PostServer();
	PostServer server2 = new PostServer();
	JSONArray json1 = null;
	private EventListAdapter adapter;
	String unm;
	TextView tv;
	boolean sts=false;
	int st;
	private static String url_eventlist ="https://ctcorphyd.com/ECOMate/events_list.php";
	private static String url_request ="https://ctcorphyd.com/ECOMate/event_request.php";
	private static final String TAG_SUCCESS = "success";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_events);
		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility( View.GONE);
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		if(b!=null) {
			unm = b.getString("unm");
				}
		setTitle("List of Events");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new eventslist_php().execute();
			}
	class eventslist_php extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( EventsList.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			String s = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = server.makeHttpRequest(url_eventlist, "POST", params);
			Log.d("JSON: ", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					sts=true;
					json1 = json.getJSONArray( "details" );
					for (int i = 0; i < json1.length(); i++) {
						JSONObject c = json1.getJSONObject( i );
						// Storing each json item in variable
						EventsView sv = new EventsView();
						sv.setSno( c.getString( "sno" ) );
						sv.setName( c.getString( "event_name" ) );
						sv.setLocation(" "+c.getString( "event_adrs" ) );
						sv.setDate(c.getString( "event_date" ) );
						sv.setThumbnailUrl( c.getString( "picture_url" ) );
						sv.setParticpant(" "+c.getString( "partp" )+" are interested. " );
						sv.setPoints(" "+c.getString( "points" )+" points");
						cList.add( sv );
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
			if(sts) {
				adapter = new EventListAdapter( EventsList.this, cList );
				listView.setAdapter( adapter );
			}
else{
				tv.setVisibility(View.VISIBLE);
				tv.setText("No Records Found");
			}
			pDialog.dismiss();
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		EventsView sv=(EventsView)cList.get(position);
		event_sno=sv.getSno();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventsList.this);
		alertDialog.setMessage("Do You Want Join this Event?");
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				new EventRequest().execute(  );
			}
		});
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
	class EventRequest extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EventsList.this);
			pDialog.setMessage("Processing...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("unm",unm));
			params.add(new BasicNameValuePair("event_sno",event_sno));
			JSONObject json = server2.makeHttpRequesturl_request,
					"POST", params);
			Log.d("Response for Register=", json.toString());
			try {
				int success = json.getInt("success");
				if (success == 1) {
					st=1;
				} else {
					st=2;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
			if(st==1) {
				Toast.makeText(getApplicationContext(), "Event Joining Request Sent..!", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Already Request Sent to this Event..!", Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.logout:
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
				break;
			case android.R.id.home:
				onBackPressed();
			default:
				break;
		}
		return true;
	}
}
