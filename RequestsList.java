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

public class RequestsList extends AppCompatActivity implements AdapterView.OnItemClickListener {

	private ProgressDialog pDialog;
	String req_sno;
	private List<RequestsView> cList = new ArrayList<RequestsView>();
	private ListView listView;
	PostServer server = new PostServer();
	PostServer server2 = new PostServer();
	JSONArray json1 = null;
	private RequestListAdapter adapter;
	String unm;

	TextView tv;
	boolean sts=false;
	int st;
	private static String url_eventreq ="https://ctcorphyd.com/ECOMate/requests_list.php";

	private static String url_aceptrequest ="https://ctcorphyd.com/ECOMate/accept_request.php";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_events);
		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility( View.GONE);
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		setTitle("Events Requests");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new eventsreq_php().execute();
			}


	class eventsreq_php extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( RequestsList.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 */
		protected String doInBackground(String... args) {
			// Building Parameters
			String s = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			JSONObject json = server.makeHttpRequest(url_eventreq, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					sts=true;
					cList.clear();
					json1 = json.getJSONArray( "details" );
					// looping through All messages
					for (int i = 0; i < json1.length(); i++) {
						JSONObject c = json1.getJSONObject( i );
						// Storing each json item in variable
						RequestsView sv = new RequestsView();
						sv.setSno( c.getString( "sno" ) );
						sv.setName("Student Name: "+c.getString( "sname" ) );
						sv.setEventname("Event: "+ c.getString( "ename" ) );
						sv.setLocation("Address: "+c.getString( "event_adrs" ) );
						sv.setDate("Date: "+c.getString( "event_date" ) );
						cList.add( sv );
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 **/
		protected void onPostExecute(String file_url) {
			if(sts) {
				adapter = new RequestListAdapter( RequestsList.this, cList );
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

		RequestsView sv=(RequestsView)cList.get(position);

		req_sno=sv.getSno();

		AlertDialog.Builder alertDialog = new AlertDialog.Builder( RequestsList.this);
		// Setting Dialog Title

		// Setting Dialog Message
		alertDialog.setMessage("Do You Want Accept this Request?");
		alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				new AcceptRequest().execute(  );

			}
		});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();





	}

	class AcceptRequest extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( RequestsList.this);
			pDialog.setMessage("Processing...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("req_sno",req_sno));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = server2.makeHttpRequest(url_aceptrequest,
					"POST", params);

			// check log cat for response
			Log.d("Response for Register=", json.toString());

			// check for success tag
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

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			if(st==1) {

				Toast.makeText(getApplicationContext(), "Accepted,Event Joining Request ..!", Toast.LENGTH_SHORT).show();
				finish();
			}

			pDialog.dismiss();
		}

	}

	/**
	 * Launching the drop-down menu bar for
	 * displaying the  view cart and logout menuitems.
	 * @param menu
	 * @return
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}




	/**
	 * Executing the specific operations based on chosen of particular switch case
	 * @param item
	 * @return
	 */

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
