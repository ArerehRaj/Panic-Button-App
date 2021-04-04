package com.example.panicbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class HelpMessageActivity extends AppCompatActivity {

    Intent intent;
    TextView title;
    TextView message;
//    Double userLat = null;
//    Double userLong = null;
    ParseGeoPoint userCords = null;
    Location messgaeLocation;

    LocationManager locationManager;
    LocationListener locationListener;

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
        setContentView(R.layout.activity_help_message);

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

        getSupportActionBar().hide();
        intent = getIntent();
        title = findViewById(R.id.messageName);
        message = findViewById(R.id.Message);

//        System.out.println(intent.getStringExtra("ContactName"));

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Contacts");
        query.whereEqualTo("Name",intent.getStringExtra("ContactName"));
        query.whereEqualTo("User", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        title.setText("Message From\n" + object.get("User").toString());
                        message.setText(object.get("Message").toString());
                        userCords = (ParseGeoPoint) object.get("LocationCords");
                    }
                }
            }
        });
    }

    public void updateLocation(Location location)
    {
        if(location != null)
        {
            messgaeLocation = location;
        }
    }

    public void goToMaps(View view)
    {
        Intent myIntent = new Intent(HelpMessageActivity.this, HelpMapsActivity.class);
        myIntent.putExtra("usersLatitude",messgaeLocation.getLatitude());
        myIntent.putExtra("usersLongitude", messgaeLocation.getLongitude());

        myIntent.putExtra("anotherLat", userCords.getLatitude());
        myIntent.putExtra("anotherLong", userCords.getLongitude());

        startActivity(myIntent);

    }
}