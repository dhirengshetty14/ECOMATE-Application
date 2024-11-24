package com.ecomate;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;

public class MainActivity extends AppCompatActivity {


        CardView admin,user;
        Intent i1,i2,i3,i4;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            admin = (CardView) findViewById(R.id.admin);
            i1 = new Intent(this,Admin.class);
            admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i1);
                }
            });


            user= (CardView) findViewById(R.id.user);
            i3 = new Intent(this,User.class);
            user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i3);
                }
            });




        }




}
