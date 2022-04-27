package com.example.studybuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SignupActivity extends Activity {

    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public void onActivityCreated(Bundle savedInstanceState) {
        // Drop down list for gender
        Spinner genderSpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SignupActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter1);


        // Drop down list for year level
        Spinner yearSpinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SignupActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter2);


        // Drop down list for faculty
        Spinner facultySpinner = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SignupActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(adapter3);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Add user images to Firebase
        setContentView(R.layout.activity_signup);
        profilePic = findViewById(R.id.profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child("images/" + randomKey);

        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image failed to upload.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00*snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                        pd.setMessage("Progress" + (int) progressPercent + "%");
                    }
                });
    }
}
