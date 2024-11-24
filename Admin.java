package com.ecomate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class Admin extends AppCompatActivity {
    TextView reg;
    EditText unm, pwd;
    String unm1,pwd1;

    SharedPreferences shp;
    SharedPreferences.Editor shpEditor;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.admin);
        unm = (EditText)findViewById(R.id.username);
        pwd = (EditText)findViewById(R.id.password);



    }

    public void logincheck(View view){


        String unm1 = unm.getText().toString();
        String pwd1 = pwd.getText().toString();
        if (null == unm1 || unm1.trim().length() == 0) {
            unm.setError("Enter  UserName");
            unm.requestFocus();
        } else if (null == pwd1 || pwd1.trim().length() == 0) {
            pwd.setError("Enter  Password");
            pwd.requestFocus();
        } else {
            if (unm1.equals("admin") && pwd1.equals("admin")) {
                Intent i = new Intent(Admin.this, AdminDashboard.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(Admin.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            }

        }


    }



    }