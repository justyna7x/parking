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
    private Button updateProfile, homePage, logout, myProfile;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ImageButton updateName, updateReg;
    private ActivityMyProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        updateName = (ImageButton) findViewById(R.id.editName);

        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder changeName = new AlertDialog.Builder(MyProfile.this);
                changeName.setTitle("Please enter your name");
                //TODO set up alert dialog to change the name and registration plate
                //final EditText nameInput
            }
        });

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