package com.example.parked2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ShowTickets extends AppCompatActivity {
    private Button homePage, myProfile, settings, purchaseTicket, showTickets, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tickets);

        showTickets = (Button) findViewById(R.id.past_tickets);
        showTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTickets.this, ShowTickets.class));

            }
        });
        purchaseTicket = (Button) findViewById(R.id.pay);
        purchaseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTickets.this, PurchaseActivity.class));
            }
        });

        settings = (Button) findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTickets.this, MapsSettings.class));
            }
        });

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ShowTickets.this, MainActivity.class));

            }
        });

        homePage = (Button) findViewById(R.id.homePage);

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTickets.this, ProfileActivity.class));

            }
        });
        myProfile = (Button) findViewById(R.id.myProfile);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShowTickets.this, MyProfile.class));

            }
        });


    }
}