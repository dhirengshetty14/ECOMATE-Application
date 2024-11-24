package com.ecomate;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class User_Registration extends AppCompatActivity {
    String name1,email1,pwd1;
    int sts = 0;
    private ProgressDialog pDialog;
    PostServer parser = new PostServer();
    EditText name, email, pwd;
    private static String url_Register = "https://ctcorphyd.com/ECOMate/user_register.php";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.user_register );
        //setTitle( "Registration" );
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById( R.id.name );
        email = findViewById( R.id.username );
        pwd= findViewById( R.id.password );
       }
    public void register(View view){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        name1=name.getText().toString();
        pwd1=pwd.getText().toString();
        email1=email.getText().toString();
        if (null == name1 || name1.trim().length() == 0) {
            name.setError("Enter Your Name");
            name.requestFocus();
        }
        else if (null == email1 || email1.trim().length() == 0) {
            email.setError("Enter  Email address");
            email.requestFocus();
        }
        else if (!email.getText().toString().trim().matches(emailPattern)) {

            Toast.makeText(this,"Incorect Email address Entry ",Toast.LENGTH_LONG).show();
        }
        else if (null == pwd1 || pwd1.trim().length() == 0) {
            pwd.setError("Enter  Password");
            pwd.requestFocus();
        }
        else{
            new Signup().execute();
        }
    }
    class Signup extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(User_Registration.this);
            pDialog.setMessage("Registering...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",name1));
            params.add(new BasicNameValuePair("pwd",pwd1));
            params.add(new BasicNameValuePair("email",email1));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = parser.makeHttpRequest(url_Register,
                    "POST", params);
            Log.d("Response for Register=", json.toString());
            try {
                int success = json.getInt("success");
                if (success == 1) {
                   Intent i = new Intent(getApplicationContext(),User.class);
                    i.putExtra("status","success");
                    startActivity(i);
                    sts=1;
                    finish();
                } else {
                    sts=2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(sts==1) {
                Toast.makeText(getApplicationContext(), "Registered Successfully..!", Toast.LENGTH_SHORT).show();
            }
            if(sts==2) {

                Toast.makeText(getApplicationContext(), "UserId already available..!", Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }
        return true;
    }
}