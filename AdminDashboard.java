package com.ecomate;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class AdminDashboard extends AppCompatActivity {

    CardView add_event,event_request,que_ans;
    Intent i1,i2,i3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.admin_home );
        setTitle( "Admin Dashboard" );

        add_event = (CardView) findViewById( R.id.add_event );
        event_request = (CardView) findViewById( R.id.event_request );
        que_ans = (CardView) findViewById( R.id.que_ans );

        i1= new Intent(AdminDashboard.this,AddEvent.class);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i1);
            }
        });

       i2= new Intent(AdminDashboard.this,RequestsList.class);
        event_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });

         i3= new Intent(AdminDashboard.this,Question_Answers.class);
        que_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i3);
            }
        });

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

            default:
                break;

        }


        return true;
    }
}
