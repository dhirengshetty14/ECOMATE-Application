package com.ecomate;

import android.app.ProgressDialog;
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

import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnswersList extends AppCompatActivity  {

	private ProgressDialog pDialog;
	String qsno,ans;
	PostServer parser = new PostServer();
	private List<QtnAnsView> cList = new ArrayList<QtnAnsView>();
	private ListView listView;
	PostServer server = new PostServer();
	PostServer server2 = new PostServer();
	JSONArray json1 = null;
	private QuestionsListAdapter adapter;
	String unm;


	TextView tv;
	boolean sts=false;

	TextInputEditText answr;
	int st;

	private static String url_answers="https://ctcorphyd.com/ECOMate/answerslist.php";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_qtns);
		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility( View.GONE);
		listView = (ListView) findViewById(R.id.list);

		Intent intent=getIntent();
		Bundle b=intent.getExtras();
		if(b!=null) {

			unm = b.getString("unm");
		}

		setTitle("List of Answers");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new answrslist_php().execute();
			}


	class answrslist_php extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( AnswersList.this);
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
			params.add(new BasicNameValuePair("unm",unm));
			// getting JSON string from URL
			JSONObject json = server.makeHttpRequest(url_answers, "POST", params);

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
						QtnAnsView sv = new QtnAnsView();
						sv.setQsno( c.getString( "qsno" ) );
						sv.setQuery("Q. "+c.getString( "query" )+"?" );
						sv.setAnswer("Ans: "+c.getString( "answr" ) );

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
				adapter = new QuestionsListAdapter( AnswersList.this, cList );
				listView.setAdapter( adapter );
			}

else{
				tv.setVisibility(View.VISIBLE);
				tv.setText("No Records Found");

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
