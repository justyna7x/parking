package com.example.parked2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {


    private Button myProfile, purchaseTicket, logout, homePage, buyTicket;
    private FirebaseAuth mAuth;
    private EditText editZone, editDuration;
    List<String> zones_list = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L");
    List<String> hours_list = Arrays.asList("1", "2", "3", "4", "5", "6");
    private DatabaseReference reference;
    private String userID;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        mAuth = FirebaseAuth.getInstance();

        editZone = (EditText) findViewById(R.id.zone);
        editDuration = (EditText) findViewById(R.id.duration);
        mAuth = FirebaseAuth.getInstance();


        buyTicket = (Button) findViewById(R.id.buy);
        buyTicket.setOnClickListener(this);

        purchaseTicket = (Button) findViewById(R.id.pay);
        purchaseTicket.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);
        homePage = (Button) findViewById(R.id.homePage);
        homePage.setOnClickListener(this);

        myProfile = (Button) findViewById(R.id.myProfile);
        myProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myProfile:
                startActivity(new Intent(this, MyProfile.class));
                break;
            case R.id.homePage:
                startActivity(new Intent(PurchaseActivity.this, ProfileActivity.class));
                break;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PurchaseActivity.this, MainActivity.class));
                break;
            case R.id.pay:
                startActivity(new Intent(PurchaseActivity.this, PurchaseActivity.class));
                break;
            case R.id.buy:
                purchasetheTicket();
                break;

        }
    }

    public void purchasetheTicket() {
        String zone = editZone.getText().toString().trim();
        String durationHours = editDuration.getText().toString().trim();
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter startTimeformat = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        String startTimeFinal = startTime.format(startTimeformat);


        if (zone.isEmpty()) {
            editZone.setError("Zone is required!");
            editZone.requestFocus();
            return;
        }
        if(durationHours.isEmpty()){
            editDuration.setError("Duration time is required!");
            editDuration.requestFocus();
        }
        if (!zones_list.contains(zone) || zone.length() != 1) {
            editZone.setError("Enter valid zone!");
            editZone.requestFocus();
            return;
        }
        if (!hours_list.contains(durationHours)) {
            editDuration.setError("Please enter the duration between 1 and 6 hours!");
            editDuration.requestFocus();
            return;

        }
        Ticket ticket = new Ticket(zone, durationHours, startTimeFinal);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(userID);
        FirebaseDatabase.getInstance().getReference(userID).child("tickets").setValue(ticket);








    }
}