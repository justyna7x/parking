package com.example.parked2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button homePage, logout, myProfile, mapsa;
    ExpandableListView expandableTextView;
    private Button purchaseTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        expandableTextView=findViewById(R.id.eTV);
        ExpandableTextViewAdapter adapter = new ExpandableTextViewAdapter(ProfileActivity.this);
        expandableTextView.setAdapter(adapter);

        homePage = (Button) findViewById(R.id.homePage);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));

            }
        });
        purchaseTicket = (Button) findViewById(R.id.pay);
        purchaseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, PurchaseActivity.class));
            }
        });

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));

            }
        });

        myProfile = (Button) findViewById(R.id.myProfile);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, MyProfile.class));

            }
        });

        mapsa = (Button) findViewById(R.id.view_zone);

        mapsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, MapsSettings.class));

            }
        });

    }
}