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
public class Rewards extends AppCompatActivity  {
	private ProgressDialog pDialog;
	String event_sno;
	private List<EventsView> cList = new ArrayList<EventsView>();
	private ListView listView;
	PostServer server = new PostServer();
	PostServer server2 = new PostServer();
	JSONArray json1 = null;
	private EventListAdapter adapter;
	String unm,points,events_cnt,sname;
	TextView point,partps,name;
	boolean sts=false;
	int st;
	private static String url_request ="https://ctcorphyd.com/ECOMate/rewards.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rewards);
		point=(TextView)findViewById( R.id.points );
		partps=(TextView)findViewById( R.id.parpt );
		name=(TextView)findViewById( R.id.name);
		point.setVisibility( View.GONE);
		partps.setVisibility( View.GONE);
		name.setVisibility( View.GONE);
		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		if(b!=null) {
			unm = b.getString("unm");
				}
		setTitle("My Rewards");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new rewards_php().execute();
			}
	class rewards_php extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( Rewards.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		protected String doInBackground(String... args) {
			String s = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("unm",unm));
			JSONObject json = server.makeHttpRequest(url_request, "POST", params);
			Log.d("JSON: ", json.toString());
			try {
				events_cnt = json.getString("events_cnt");
				points = json.getString("points");

				sname = json.getString("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url) {
				point.setVisibility(View.VISIBLE);
			partps.setVisibility(View.VISIBLE);
			name.setVisibility(View.VISIBLE);
				point.setText("\nTotal\n"+points+"\n  points earned!");
			partps.setText("Participated in\n"+events_cnt+"\nevents");
			name.setText("Welcome "+sname);
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
public void check(View view) {
	Intent i=new Intent(Rewards.this,LeaderBoard.class);
	startActivity(i);
}
}

