package com.example.panicbutton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class VolunteerFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner genderSpinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    String selectedGender = null;
    EditText editTextVolName;
    EditText editTextVolAge;
    EditText editTextVolAddress;
    LocationManager locationManager;
    LocationListener locationListener;

    Location usersLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateLocation(lastKnownLocation);
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_form);

        getSupportActionBar().hide();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        editTextVolName = findViewById(R.id.editTextVolunteerName);
        editTextVolAge = findViewById(R.id.editVolunteerAge);
        editTextVolAddress = findViewById(R.id.editVolunteerAddress);

        genderSpinner = findViewById(R.id.genderDropDownVol);
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.genders, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocation(lastKnownLocation);
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedGender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateLocation(Location lastKnownLocation)
    {
        if(lastKnownLocation != null)
        {
            usersLocation = lastKnownLocation;
        }
    }

    public void submitForm(View view)
    {

        String volName = editTextVolName.getText().toString();
        String volAge = editTextVolAge.getText().toString();
        String volAddress = editTextVolAddress.getText().toString();

        if(volName.isEmpty())
        {
            editTextVolName.setError("Please Enter Your Name.");
            return;
        }

        if(volAge.isEmpty())
        {
            editTextVolAge.setError("Please Enter Your Age.");
            return;
        }

        if(volAddress.isEmpty())
        {
            editTextVolAddress.setError("Please Enter Your Address.");
            return;
        }

        new AlertDialog.Builder(VolunteerFormActivity.this)
                .setTitle("Submit the Form?")
                .setMessage("Are you sure you want to submit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseObject volunteer = new ParseObject("Volunteers");

                        volunteer.put("Name", volName);
                        volunteer.put("Age", volAge);
                        volunteer.put("Address", volAddress);
                        volunteer.put("Gender", selectedGender);

                        ParseGeoPoint usersGeoPoint = new ParseGeoPoint(usersLocation.getLatitude(), usersLocation.getLongitude());
                        volunteer.put("Location", usersGeoPoint);

                        volunteer.saveInBackground();

                        Intent intent = new Intent(VolunteerFormActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Lemme Think", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


}