package com.example.parked2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button homePage, logout, myProfile, mapsa, showTickets, viewAllTickets;
    ExpandableListView expandableTextView;
    private Button purchaseTicket;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

       // getSupportFragmentManager().beginTransaction().add(R.id.pudelko, new TicketsFragment());

        viewAllTickets=findViewById(R.id.viewAllTickets);
        viewAllTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.pudelko, new AllTheTickets()).commit();
                expandableTextView.setVisibility(View.GONE);
                viewAllTickets.setVisibility(View.GONE);


            }
        });

        if(!userID.equals("yE7bfAUWkcRDTyOqXgzQR3Pwvvg1")){
            viewAllTickets.setVisibility(View.GONE);

        }

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
        showTickets = (Button) findViewById(R.id.past_tickets);
        showTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.pudelko, new TicketsFragment()).commit();
                expandableTextView.setVisibility(View.GONE);
                /*mapsa.setVisibility(View.GONE);
                purchaseTicket.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                myProfile.setVisibility(View.GONE);
                showTickets.setVisibility(View.GONE);
                homePage.setVisibility(View.GONE);*/

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