package com.ecomate;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AddEvent extends AppCompatActivity {

    TextInputEditText ename,eadrs,points;

    private ProgressDialog pDialog;
    PostServer server = new PostServer();
    private Bitmap bitmap;
    String ename1,eadrs1,points1,edate;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

    private static String url_addevents ="https://ctcorphyd.com/ECOMate/add_events.php";

    ImageView imageView;
    EditText event_date;
    int count=0;
    int sts=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_events );
        setTitle( "Add Event Info" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ename = findViewById(R.id.ename);
        eadrs = findViewById(R.id.eadrs);
        points = findViewById(R.id.points);
        event_date=(EditText)findViewById(R.id.date);
        imageView = (ImageView) findViewById(R.id.imageView);


    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public  void showFileChooser(View view) {
        count=1;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 250, 250, false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void feedback_form(View view){


        ename1=ename.getText().toString();
        eadrs1=eadrs.getText().toString();
       points1=points.getText().toString();
        edate=event_date.getText().toString();


        if (null == ename1 || ename1.trim().length() == 0) {
            ename.setError( "Enter  Event name" );
            ename.requestFocus();
        } else if (null == eadrs1 || eadrs1.trim().length() == 0) {
            eadrs.setError( "Event Address" );
            eadrs.requestFocus();
        }
        else if (null == edate || edate.trim().length() == 0) {
            event_date.setError( "Pick Date" );
            event_date.requestFocus();
        }

        else if (null == points1 || points1.trim().length() == 0) {
            points.setError( "Enter Points " );
            points.requestFocus();

        }
        else if (count == 0) {
            Toast.makeText( this, "Choose Event Picture", Toast.LENGTH_LONG ).show();

        }
        else {


            new AddEventPHP().execute( bitmap );

            return;
        }



    }


    class AddEventPHP extends AsyncTask<Bitmap, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddEvent.this);
            pDialog.setMessage("Processing...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(Bitmap... img) {

            // Building Parameters
            Bitmap bitmap = img[0];
            String uploadImage = getStringImage(bitmap);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ename",ename1));
            params.add(new BasicNameValuePair("eadrs",eadrs1));
            params.add(new BasicNameValuePair("edate",edate));
            params.add(new BasicNameValuePair("points",points1));
            params.add(new BasicNameValuePair("image",uploadImage));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = server.makeHttpRequest(url_addevents,
                    "POST", params);


            Log.d("Response r", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");


                if (success == 1) {

                    sts=1;

                }
                if (success == 2) {

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
                Toast.makeText(AddEvent.this,"New Event Posted Successfully..!",Toast.LENGTH_LONG ).show();
                finish();
            }

            pDialog.dismiss();
        }

    }


    public String getStringImage(Bitmap bmp){

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress( Bitmap.CompressFormat.JPEG, 100, baos );
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString( imageBytes, Base64.DEFAULT );

            return encodedImage;
        } catch (Exception e) {
            Log.d("Excep=",e.toString());
        }
        return "";
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