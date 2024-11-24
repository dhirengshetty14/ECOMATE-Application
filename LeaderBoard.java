package com.ecomate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class LeaderBoard extends AppCompatActivity  {

	private ProgressDialog pDialog;
	String req_sno;
	private List<RequestsView> cList = new ArrayList<RequestsView>();
	private ListView listView;
	PostServer server = new PostServer();

	JSONArray json1 = null;
	private RequestListAdapter adapter;
	String sname;
	int textSize = 0, smallTextSize =0;

	TableLayout tl;

	ArrayList al=new ArrayList();
	boolean sts=false;
	TextView name;
	int st;
	private static String url_leaderborad ="https://ctcorphyd.com/ECOMate/leader_board_list.php";
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leader_board);

		name=(TextView)findViewById(R.id.name);

		setTitle("Leader Board");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
		smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);


		tl = (TableLayout) findViewById(R.id.main_table);
		tl.setStretchAllColumns(true);


		TableRow tr_head = new TableRow(this);
		//tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));


		TextView tv1 = new TextView(this);
		tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tv1.setGravity( Gravity.LEFT);
		tv1.setPadding(5, 15, 0, 15);
		tv1.setBackgroundColor( Color.parseColor("#f7f7f7"));
		tv1.setText("Rank");
		tv1.setTextSize( TypedValue.COMPLEX_UNIT_PX, smallTextSize);


		tr_head.addView(tv1);// add the column to the table row here

		TextView tv2 = new TextView(this);
		// define id that must be unique
		tv2.setText("Name"); // set the text for the header
		// set the color
		tv2.setBackgroundColor(Color.parseColor("#f0f0f0"));
		tv2.setPadding(5, 5, 0, 5);
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
		// set the padding (if required)
		tr_head.addView(tv2); // add the column to the table row here

		TextView tv3 = new TextView(this);
		tv3.setText("Points");

		tv3.setBackgroundColor(Color.parseColor("#f7f7f7"));
		tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
		tv1.setPadding(5, 5, 5, 5);
		tr_head.addView(tv3);// add the column to the table row here


		tl.addView(tr_head, new TableLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		new leaderboard_php().execute();
			}


	class leaderboard_php extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( LeaderBoard.this);
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
			JSONObject json = server.makeHttpRequest(url_leaderborad, "POST", params);

			// Check your log cat for JSON reponse
			Log.d("JSON: ", json.toString());

			try {
				int success = json.getInt(TAG_SUCCESS);

				sname = json.getString("name");

				if (success == 1) {
					Integer count=0;
					sts=true;
					al.clear();
					json1 = json.getJSONArray( "details" );

					for (int i = 0; i < json1.length(); i++) {
						JSONObject c = json1.getJSONObject( i );

						String name=c.getString( "name" );// get the first variable
						String points=c.getString( "points" );
						String rank=c.getString( "rank" );

						al.add(rank+","+name+","+points);






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
					int count=0;
			for(int i=0;i<al.size();i++)

			{
				String val[]=al.get( i ).toString().split( "," );
				// Create the table row
				TableRow tr = new TableRow(LeaderBoard.this);
				tr.setBackgroundColor(Color.GRAY);
				tr.setId(100+count);
				tr.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));


				//Create two columns to add as table data
				// Create a TextView to add date
				TextView dat = new TextView(LeaderBoard.this);
				dat.setId(200+count);
				dat.setText(val[0]);
				dat.setBackgroundColor(Color.parseColor("#ffffff"));
				dat.setTextColor(Color.parseColor("#000000"));
				dat.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				tr.addView(dat);

				TextView recipnt = new TextView(LeaderBoard.this);
				recipnt.setId(200+count);
				recipnt.setText(val[1]);
				recipnt.setBackgroundColor(Color.parseColor("#f8f8f8"));
				recipnt.setTextColor(Color.parseColor("#000000"));
				recipnt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				tr.addView(recipnt);

				TextView amnt = new TextView(LeaderBoard.this);
				amnt.setId(200+count);
				amnt.setText(val[2]);
				amnt.setPadding(2, 0, 5, 0);
				amnt.setBackgroundColor(Color.parseColor("#ffffff"));
				amnt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
				amnt.setTextColor(Color.parseColor("#000000"));
				tr.addView(amnt);

				// finally add this to the table row
				tl.addView(tr, new TableLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				count++;
			}
		name.setText( sname );

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
