package com.example.studybuddy;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studybuddy.ui.explore.ExploreFragment;
import com.example.studybuddy.ui.explore.ExploreViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private Button loginButton, signUpButton;

    private ProgressDialog loading;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user != null) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signUp);
        loading = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getText().toString().trim();
                final String password = loginPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required.");
                    return;
                } else {
                    loading.setMessage("Login in...");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loading.dismiss();
                        }
                    });
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

}

//package com.example.studybuddy;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.studybuddy.ui.explore.ExploreFragment;
//import com.example.studybuddy.ui.explore.ExploreViewModel;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText edit_email, edit_password;
//
//    private Button loginButton;
//    private Button signUpButton;
//
//    private String email, password;
//
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        edit_email = findViewById(R.id.email);
//        edit_password = findViewById(R.id.password);
//
//        loginButton = findViewById(R.id.login);
//        signUpButton = findViewById(R.id.signUp);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                email = edit_email.getText().toString();
//                password = edit_password.getText().toString();
//
//                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                    Toast.makeText(getApplicationContext(), "Please enter email and password.", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                mAuth = FirebaseAuth.getInstance();
//
//                if (mAuth.getCurrentUser() != null) {
//                    updateUI(mAuth.getCurrentUser());
//                }
//
//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful()) {
//                                    Log.d("MainActivity", "signInWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
//                                } else {
//                                    Log.w("MainActivity", "createUserWithEmail:failure", task.getException());
//                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
//            }
//        });
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            updateUI(currentUser);
//        }
//    }
//
//    public void updateUI(FirebaseUser currentUser) {
//        Intent profileIntent = new Intent(getApplicationContext(), HomeActivity.class);
//        profileIntent.putExtra("email", currentUser.getEmail());
//        Log.v("Data", currentUser.getUid());
//        startActivity(profileIntent);
//    }
//
//}