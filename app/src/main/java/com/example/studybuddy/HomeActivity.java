package com.example.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studybuddy.databinding.ActivityHomeBinding;
import com.example.studybuddy.model.DAOUser;
import com.example.studybuddy.model.Pin;
import com.example.studybuddy.model.User;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHomeBinding binding;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private ArrayAdapter<String> subjectIDs;
    private AutoCompleteTextView subjectID;
    private TextInputEditText courseNumber;
    private TextInputEditText locationSpecifics;
    private DAOUser dao = new DAOUser();
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    createNewStudySpace();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ubcCoords = new LatLng(49.2615, -123.2500);
        //mMap.addMarker(new MarkerOptions().position(ubcCoords).title("Marker at UBC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubcCoords));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createNewStudySpace() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View newStudyPlacePopup = getLayoutInflater().inflate(R.layout.new_study_space, null);
        dialogBuilder.setView(newStudyPlacePopup);



        dialogBuilder.setPositiveButton("Add Pin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                testPlacePin2();
                dialog.dismiss();
            }
        });

        subjectID = (AutoCompleteTextView) findViewById(R.id.course_subject_actv);
        subjectIDs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.subject));
        subjectID.setAdapter(subjectIDs);

        courseNumber = (TextInputEditText) findViewById(R.id.course_number_actv);
        locationSpecifics = (TextInputEditText) findViewById(R.id.location_specifics);

        dialog = dialogBuilder.create();
        dialog.show();




    }

    @Override
    public View findViewById(int i) {
        return getLayoutInflater().inflate(R.layout.new_study_space, null).findViewById(i);
    }

    public void testPlacePin2() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                LatLng curLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                try {
                    Pin pin = new Pin(subjectID.getText().toString(), Integer.parseInt(courseNumber.getText().toString()),
                            curLatLng, locationSpecifics.getText().toString(), null);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                mMap.addMarker(new MarkerOptions().position(curLatLng)).setTitle(subjectID.getText().toString() + " " + courseNumber.getText().toString());

                //Toast.makeText(getApplicationContext(), "curLocation is " + "(" + location.getLatitude() + ", " + location.getLongitude() + ")", Toast.LENGTH_LONG).show();
            }
        });
    }

}