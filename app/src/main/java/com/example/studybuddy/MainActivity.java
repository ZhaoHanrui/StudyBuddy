package com.example.studybuddy;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studybuddy.ui.explore.ExploreFragment;
import com.example.studybuddy.ui.explore.ExploreViewModel;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signUp);

        Context i = getApplicationContext();

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(i, HomeActivity.class));
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Null Pointer", Toast.LENGTH_SHORT).show();
                } catch (RuntimeException e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(i, SignupActivity.class));
            }
        });

    }

}