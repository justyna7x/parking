package com.example.parked2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parked2.databinding.ActivityMyProfileBinding;
import com.example.parked2.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyProfile extends AppCompatActivity {
    private Button updateProfile, homePage, logout, myProfile, setPassword, setFullName;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ActivityMyProfileBinding binding;
    private Button purchaseTicket;
    private EditText editName, editPassword;
    private FirebaseAuth mAuth;
    private Button showTickets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mAuth = FirebaseAuth.getInstance();

        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.password);
        setFullName = (Button)findViewById(R.id.setNAme);
        setFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNAme();
            }
        });

        setPassword = (Button) findViewById(R.id.setPassword);
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword1();
            }
        });

        purchaseTicket = (Button) findViewById(R.id.pay);
        purchaseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, PurchaseActivity.class));
            }
        });
        showTickets = (Button) findViewById(R.id.past_tickets);
        showTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfile.this, ShowTickets.class));

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


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile !=null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;

                    greetingTextView.setText("Welcome, " + fullName+"!");
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updatePassword1() {
        String haslo = editPassword.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(haslo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MyProfile.this, "Password has been updated successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setNAme() {String FullName = editName.getText().toString().trim();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("fullName").setValue(FullName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MyProfile.this, "Your name has been updated successfully", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MyProfile.this, "Something went wrong! Try again", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void updateData(String fullName) {

        HashMap User = new HashMap();
        User.put("fullName", fullName);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){
                    binding.fullName.setText("");
                    Toast.makeText(MyProfile.this, "Data updated successfully", Toast.LENGTH_LONG).show();

                }else{Toast.makeText(MyProfile.this, "Failed to update", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}