package com.example.parked2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parked2.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateProfile extends AppCompatActivity {

    private ActivityUpdateProfileBinding binding;
    private Button homePage,logout, myProfile;
    //private EditText etName, etRegPlate, etPassword;
    //private Button updateBtn;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = binding.fullName.getText().toString();
                String regPlate = binding.regPlate.getText().toString();

                updateData(fullName, regPlate);


            }
        });

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UpdateProfile.this, MainActivity.class));

            }
        });

        homePage = (Button) findViewById(R.id.homePage);
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, ProfileActivity.class));

            }
        });

        myProfile = (Button) findViewById(R.id.myProfile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, MyProfile.class));
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final EditText editTextFullname = (EditText) findViewById(R.id.fullName);
        final EditText editTextRegPlate = (EditText) findViewById(R.id.regPlate);


    }

    private void updateData(String fullName, String regPlate) {

        HashMap User = new HashMap();
        User.put("fullName", fullName);
        User.put("regPlate", regPlate);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){
                    binding.fullName.setText("");
                    binding.regPlate.setText("");
                    Toast.makeText(UpdateProfile.this, "Data updated successfully", Toast.LENGTH_LONG).show();

                }else{Toast.makeText(UpdateProfile.this, "Failed to update", Toast.LENGTH_LONG).show();

                }

            }
        });
    }


}