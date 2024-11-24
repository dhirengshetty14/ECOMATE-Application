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

import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Question_Answers extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
	private static String url_qtnslist ="https://ctcorphyd.com/ECOMate/questions_list.php";

	private static String url_answers="https://ctcorphyd.com/ECOMate/answers.php";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_qtns);
		tv=(TextView)findViewById(R.id.textView);
		tv.setVisibility( View.GONE);
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		setTitle("List of Queries");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new querylist_php().execute();
			}


	class querylist_php extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( Question_Answers.this);
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
			JSONObject json = server.makeHttpRequest(url_qtnslist, "POST", params);

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
						sv.setAnswer("Student Name: "+c.getString( "sname" ) );

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
				adapter = new QuestionsListAdapter( Question_Answers.this, cList );
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

		QtnAnsView sv=(QtnAnsView)cList.get(position);

		qsno=sv.getQsno();

		AlertDialog.Builder builder = new AlertDialog.Builder(Question_Answers.this);
		builder.setTitle("Answer");

		// set the custom layout
		final View customLayout = getLayoutInflater().inflate(R.layout.questions, null);
		builder.setView(customLayout);

		// add a button
		builder.setPositiveButton("OK", (dialog, which) -> {
			// send data from the AlertDialog to the Activity
			answr = customLayout.findViewById(R.id.query);
			ans=answr.getText().toString();
			new sendanswer(  ).execute();
		});
		// create and show the alert dialog
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	class sendanswer extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Question_Answers.this);
			pDialog.setMessage("Processing...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			// Building Parameters

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ans",ans));
			params.add(new BasicNameValuePair("qsno",qsno));
			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = parser.makeHttpRequest(url_answers,
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

				Toast.makeText(getApplicationContext(), "Answered Successfully..!", Toast.LENGTH_SHORT).show();

				Intent i=new Intent(Question_Answers.this,AdminDashboard.class);
				startActivity(i);
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
