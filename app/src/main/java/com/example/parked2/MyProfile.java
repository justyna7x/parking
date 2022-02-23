package com.example.parked2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {
    private Button updateProfile;
    private Button homePage;
    private Button logout;
    private Button myProfile;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        updateProfile = (Button) findViewById(R.id.edit_profile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, UpdateProfile.class));

            }
        });

        homePage = (Button) findViewById(R.id.homePage);
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MyProfile.this, ProfileActivity.class));

            }
        });

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MyProfile.this, MainActivity.class));

            }
        });

        myProfile = (Button) findViewById(R.id.myProfile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyProfile.this, MyProfile.class));

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);
        final TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
        final TextView emailTextView = (TextView) findViewById(R.id.emailAddress);
        final TextView regTextView = (TextView) findViewById(R.id.reg);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile !=null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String reg = userProfile.regPlate;

                    greetingTextView.setText("Welcome, " + fullName+"!");
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    regTextView.setText(reg);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });
    }
}