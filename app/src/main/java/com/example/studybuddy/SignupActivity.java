package com.example.studybuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studybuddy.model.User;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends Activity {

    private EditText registerFirstName, registerLastName, registerEmail, registerPassword;
    private Spinner registerGender, registerYear, registerFaculty;

    private CircleImageView profileImage;

    private Button confirmButton, backButton;

    private Uri resultUri;
    private ProgressDialog loading;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        profileImage = findViewById(R.id.profile_picture);
        registerFirstName = findViewById(R.id.firstName);
        registerLastName = findViewById(R.id.lastName);
        registerEmail = findViewById(R.id.email);
        registerPassword = findViewById(R.id.password);
        registerGender = findViewById(R.id.gender);
        registerYear = findViewById(R.id.year);
        registerFaculty = findViewById(R.id.faculty);
        confirmButton = findViewById(R.id.profileConfirmButton);
        backButton = findViewById(R.id.backButton);
        loading = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = registerFirstName.getText().toString().trim();
                final String lastName = registerLastName.getText().toString().trim();
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String gender = registerGender.getSelectedItem().toString().trim();
                final String year = registerYear.getSelectedItem().toString().trim();
                final String faculty = registerFaculty.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    registerFirstName.setError("First name is required.");
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    registerLastName.setError("Last name is required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    registerEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerPassword.setError("Password is required.");
                    return;
                }
                if (gender.equals("Gender")) {
                    Toast.makeText(SignupActivity.this, "Gender is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (year.equals("Year")) {
                    Toast.makeText(SignupActivity.this, "Year is required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (faculty.equals("Faculty")) {
                    Toast.makeText(SignupActivity.this, "Faculty is required.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    loading.setMessage("Registering...");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                String error = task.getException().toString();
                                Toast.makeText(SignupActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                            } else {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(currentUserID);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserID);
                                userInfo.put("firstName", firstName);
                                userInfo.put("lastName", lastName);
                                userInfo.put("email", email);
                                userInfo.put("password", password);
                                userInfo.put("gender", gender);
                                userInfo.put("year", year);
                                userInfo.put("faculty", faculty);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "User added successfully.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                        loading.dismiss();
                                    }
                                });

                                if(resultUri != null) {
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("Profile Images").child(currentUserID);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilePictureUrl", imageUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(SignupActivity.this, "Image URL added.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loading.dismiss();
                                }
                            }
                        }
                    });
                }
            }
        });

        backButton.setOnClickListener((view) -> {
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);
        }
    }
}

//public class SignupActivity extends Activity {
//
//    private User user;
//
//    private EditText edit_first_name, edit_last_name, edit_email, edit_password;
//    private Spinner edit_gender, edit_year, edit_faculty;
//    private Button confirmButton;
//
//    private FirebaseDatabase database;
//    private DatabaseReference mDatabase;
//    private FirebaseAuth mAuth;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        edit_first_name = findViewById(R.id.firstName);
//        edit_last_name = findViewById(R.id.lastName);
//        edit_gender = findViewById(R.id.gender);
//        edit_year = findViewById(R.id.year);
//        edit_faculty = findViewById(R.id.faculty);
//        edit_email = findViewById(R.id.email);
//        edit_password = findViewById(R.id.password);
//        confirmButton = findViewById(R.id.profileConfirmButton);
//
//        database = FirebaseDatabase.getInstance();
//        mDatabase = database.getReference("Users");
//        mAuth = FirebaseAuth.getInstance();
//
//        Spinner genderSpinner = (Spinner) findViewById(R.id.gender);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SignupActivity.this,
//                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        genderSpinner.setAdapter(adapter1);
//
//        Spinner yearSpinner = (Spinner) findViewById(R.id.year);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SignupActivity.this,
//                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year));
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        yearSpinner.setAdapter(adapter2);
//
//        Spinner facultySpinner = (Spinner) findViewById(R.id.faculty);
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SignupActivity.this,
//                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.faculty));
//        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        facultySpinner.setAdapter(adapter3);
//
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = edit_email.getText().toString().trim();
//                String password = edit_password.getText().toString().trim();
//
//                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                    Toast.makeText(getApplicationContext(), "Please enter email and password.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String firstName = edit_first_name.getText().toString().trim();
//                String lastName = edit_last_name.getText().toString().trim();
//                String gender = edit_gender.getSelectedItem().toString();
//                String year = edit_year.getSelectedItem().toString();
//                String faculty = edit_faculty.getSelectedItem().toString();
//
//                user = new User(firstName, lastName, gender, year, faculty, email, password);
//                registerUser(email, password);
//            }
//        });
//    }
//
//    public void registerUser(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()) {
//                            Log.d("SignupActivity", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            Log.w("SignupActivity", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    public void updateUI(FirebaseUser currentUser) {
//        String keyID = mDatabase.push().getKey();
//        mDatabase.child(keyID).setValue(user);
//        Intent loginIntent = new Intent(this, MainActivity.class);
//        startActivity(loginIntent);
//    }
//
//}