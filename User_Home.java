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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User_Home extends AppCompatActivity {


        CardView view_event,confirm,rewards,que_ans;
        Intent i1,i2,i3,i4;

    TextInputEditText query;

    String qtn;
    int sts=0;

    private ProgressDialog pDialog;
    PostServer parser = new PostServer();
    private static String url_query = "https://ctcorphyd.com/ECOMate/user_query.php";
    final String[] option = {"Query","Answers"};
    String unm;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.user_home);
            setTitle( "User Dashboard" );
            Intent intent=getIntent();
            Bundle b=intent.getExtras();
            if(b!=null) {

                unm = b.getString("unm");
            }

            view_event= (CardView) findViewById(R.id.view_event);
            i1 = new Intent(this,EventsList.class);
            i1.putExtra( "unm",unm );
            view_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i1);
                }
            });

            confirm= (CardView) findViewById(R.id.confirm);

            i2 = new Intent(this,AcceptedRequestsList.class);
            i2.putExtra( "unm",unm );
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i2);
                }
            });

            rewards= (CardView) findViewById(R.id.rewards);

            i3 = new Intent(this,Rewards.class);
            i3.putExtra( "unm",unm );
            rewards.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i3);
                }
            });


            que_ans= (CardView) findViewById(R.id.que_ans);

            //i4 = new Intent(this,Rewards.class);
           // i4.putExtra( "unm",unm );
            que_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(User_Home.this,android.R.layout.select_dialog_item, option);
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Home.this);
                    builder.setTitle("Select Option");
                    builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            if (i == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(User_Home.this);
                                builder.setTitle("Query");

                                // set the custom layout
                                final View customLayout = getLayoutInflater().inflate(R.layout.questions, null);
                                builder.setView(customLayout);

                                // add a button
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    // send data from the AlertDialog to the Activity
                                   query = customLayout.findViewById(R.id.query);
                                    qtn=query.getText().toString();
                                  new sendquery(  ).execute();
                                });
                                // create and show the alert dialog
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {

                                Intent i2 = new Intent(getApplicationContext(),AnswersList.class);
                                i2.putExtra("unm",unm);
                                startActivity(i2);

                            }

                        }
                    });


                    final  AlertDialog a = builder.create();
                    a.show();





                }
            });


        }

    class sendquery extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(User_Home.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("query",qtn));
            params.add(new BasicNameValuePair("unm",unm));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = parser.makeHttpRequest(url_query,
                    "POST", params);

            // check log cat for response
            Log.d("Response for Register=", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");


                if (success == 1) {

                    sts=1;
                     } else {
                    sts=2;
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
            if(sts==1) {

                Toast.makeText(getApplicationContext(), "Query Sent..!", Toast.LENGTH_SHORT).show();
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
            default:
                break;

        }


        return true;
    }




}
