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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class MyProfile extends AppCompatActivity implements View.OnClickListener{
    private Button homePage, logout, myProfile, setPassword, setFullName, showTickets, purchaseTicket;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ActivityMyProfileBinding binding;
    private EditText editName, editPassword;
    private FirebaseAuth mAuth;
    private TextView greetingTextView, fullNameTextView, emailTextView, emailAddressTitle, FullnameTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mAuth = FirebaseAuth.getInstance();

        editName = (EditText) findViewById(R.id.edit_name);
        editPassword = (EditText) findViewById(R.id.password);
        setFullName = (Button)findViewById(R.id.setNAme);
        setFullName.setOnClickListener(this);
        emailAddressTitle = (TextView) findViewById(R.id.emailAddressTitle);
        FullnameTitle = (TextView) findViewById(R.id.fullNameTitle);

        showTickets = (Button) findViewById(R.id.past_tickets);
        showTickets.setOnClickListener(this);

        setPassword = (Button) findViewById(R.id.setPassword);
        setPassword.setOnClickListener(this);

        purchaseTicket = (Button) findViewById(R.id.pay);
        purchaseTicket.setOnClickListener(this);

        homePage = (Button) findViewById(R.id.homePage);
        homePage.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);

        myProfile = (Button) findViewById(R.id.myProfile);
        myProfile.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

       greetingTextView = (TextView) findViewById(R.id.greeting);
        fullNameTextView = (TextView) findViewById(R.id.fullName);
        emailTextView = (TextView) findViewById(R.id.emailAddress);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile !=null && userProfile.fullName!=null){
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;

                    greetingTextView.setText("Welcome, " + fullName+"!");
                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);

                }
                if (userID.equals("yE7bfAUWkcRDTyOqXgzQR3Pwvvg1")){
                    editName.setVisibility(View.GONE);
                    editPassword.setVisibility(View.GONE);
                    setFullName.setVisibility(View.GONE);
                    setPassword.setVisibility(View.GONE);
                }
               else  {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String personName = firebaseUser.getDisplayName();
                    String personEmail = firebaseUser.getEmail();
                    greetingTextView.setText("Welcome, " + personName + "!");
                    fullNameTextView.setText(personName);
                    emailTextView.setText(personEmail);
                    editName.setVisibility(View.GONE);
                    editPassword.setVisibility(View.GONE);
                    setFullName.setVisibility(View.GONE);
                    setPassword.setVisibility(View.GONE);


                }
                }
            


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });
        showTickets = (Button) findViewById(R.id.past_tickets);
        showTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.pudelko, new TicketsFragment()).commit();
                setFullName.setVisibility(View.GONE);
                setPassword.setVisibility(View.GONE);
                editPassword.setVisibility(View.GONE);
                editName.setVisibility(View.GONE);
                greetingTextView.setVisibility(View.GONE);
                fullNameTextView.setVisibility(View.GONE);
                emailTextView.setVisibility(View.GONE);
                emailAddressTitle.setVisibility(View.GONE);
                FullnameTitle.setVisibility(View.GONE);

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myProfile:
                startActivity(new Intent(this, MyProfile.class));
                break;
            case R.id.homePage:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.pay:
                startActivity(new Intent(this, PurchaseActivity.class));
                break;
            case R.id.setNAme:
                setNAme();
                break;

            case R.id.setPassword:
                updatePassword1();
                break;

        }
    }

    private void updatePassword1() {
        String haslo = editPassword.getText().toString().trim();

        if(haslo.isEmpty()){
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }
        if(haslo.length()<6){
            editPassword.setError("Password must have min 6 characters");
            editPassword.requestFocus();
            return;
        }



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
        if(FullName.isEmpty()){
            editName.setError("Full Name is required");
            editName.requestFocus();
            return;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
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

}