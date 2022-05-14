package com.example.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends Activity {
    private TextView name, gender, year, faculty, email;
    private CircleImageView profileImage;
    private Button homeButton, exploreButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.display_name);
        gender = findViewById(R.id.display_gender);
        year = findViewById(R.id.display_year);
        faculty = findViewById(R.id.display_faculty);
        email = findViewById(R.id.display_email);
        profileImage = findViewById(R.id.display_image);
        homeButton = findViewById(R.id.homeButton);
        exploreButton = findViewById(R.id.exploreButton);
        logoutButton = findViewById(R.id.logoutButton);

        homeButton.setOnClickListener((view) -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        exploreButton.setOnClickListener((view) -> {
            Intent intent = new Intent(ProfileActivity.this, ExploreActivity.class);
            startActivity(intent);
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    name.setText(snapshot.child("firstName").getValue().toString() + " " + snapshot.child("lastName").getValue().toString());
                    gender.setText(snapshot.child("gender").getValue().toString());
                    year.setText(snapshot.child("year").getValue().toString());
                    faculty.setText(snapshot.child("faculty").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());

                    Glide.with(getApplicationContext()).load(snapshot.child("profilePictureUrl").getValue().toString()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
