package com.example.panicbutton;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.View;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanicButtonActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location userDangerLocation = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);

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

    private void updateLocation(Location location)
    {
        userDangerLocation = location;
    }

    public void Danger(View view)
    {
        new AlertDialog.Builder(PanicButtonActivity.this)
                .setTitle("Are you sure you want to send the Signal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Need immediate Help... Please Send Help.\n>" + ParseUser.getCurrentUser().getUsername();
                        ParseQuery<ParseObject> contacts = new ParseQuery<ParseObject>("Contacts");
                        contacts.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

                        contacts.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null)
                                {
                                    for(ParseObject object : objects)
                                    {
                                        object.put("Message",message);
                                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(userDangerLocation.getLatitude(), userDangerLocation.getLongitude());
                                        object.put("LocationCords", parseGeoPoint);

                                        object.saveInBackground();
                                    }
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void Sus(View view)
    {
        new AlertDialog.Builder(PanicButtonActivity.this)
                .setTitle("Are you sure you want to send the Signal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Feeling unsafe/suspicious right now\n>" + ParseUser.getCurrentUser().getUsername();
                        ParseQuery<ParseObject> contacts = new ParseQuery<ParseObject>("Contacts");
                        contacts.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

                        contacts.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null)
                                {
                                    for(ParseObject object : objects)
                                    {
                                        object.put("Message",message);
                                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(userDangerLocation.getLatitude(), userDangerLocation.getLongitude());
                                        object.put("LocationCords", parseGeoPoint);

                                        object.saveInBackground();
                                    }
                                }
                            }
                        });

                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Police");

                        ParseGeoPoint userParseGeoPoint = new ParseGeoPoint(userDangerLocation.getLatitude(), userDangerLocation.getLongitude());
                        query.whereNear("Location",userParseGeoPoint);
                        query.setLimit(10);

//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> objects, ParseException e) {
//                                if(e == null)
//                                {
//                                    for(ParseObject object : objects)
//                                    {
//                                        ParseGeoPoint centersLocation = (ParseGeoPoint) object.get("Location");
//                                        if(centersLocation != null)
//                                        {
//                                            double distanceInKms = userParseGeoPoint.distanceInKilometersTo(centersLocation);
//                                            double distanceAccuracy = (double) Math.round(distanceInKms * 10) / 10;
//
//                                            if(distanceAccuracy < 10.0)
//                                            {
//                                                ParseObject helpTOPolice = new ParseObject("PoliceComplaints");
//                                                helpTOPolice.put("PoliceStation",object.get("Name").toString());
//                                                helpTOPolice.put("User", ParseUser.getCurrentUser().getUsername());
//                                                helpTOPolice.put("Message", message);
//                                                helpTOPolice.put("PoliceCords", centersLocation);
////                                                helpTOPolice.put("Users");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void safe(View view)
    {
        new AlertDialog.Builder(PanicButtonActivity.this)
                .setTitle("Are you sure you want to send the Signal?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = "Safe at current location....\n>" + ParseUser.getCurrentUser().getUsername();
                        ParseQuery<ParseObject> contacts = new ParseQuery<ParseObject>("Contacts");
                        contacts.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

                        contacts.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e == null)
                                {
                                    for(ParseObject object : objects)
                                    {
                                        object.put("Message",message);
                                        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(userDangerLocation.getLatitude(), userDangerLocation.getLongitude());
                                        object.put("LocationCords", parseGeoPoint);

                                        object.saveInBackground();
                                    }
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}