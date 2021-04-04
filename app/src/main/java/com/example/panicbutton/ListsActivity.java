package com.example.panicbutton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListsActivity extends AppCompatActivity {

    ListView myListView;
    SimpleAdapter simpleAdapter;
    TextView title;

    List<String> name = new ArrayList<>();
    List<Double> latitude = new ArrayList<>();
    List<Double> longitude = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;

    int code = 0;

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
                    updateListView(lastKnownLocation);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        getSupportActionBar().hide();

        myListView = findViewById(R.id.myListsView);
        title = findViewById(R.id.listsTitle);

        Intent myIntent = getIntent();
        code = myIntent.getIntExtra("Code",-1);
        title.setText(myIntent.getStringExtra("title"));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateListView(location);
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
                updateListView(lastKnownLocation);
            }
        }

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ListsActivity.this, BookCenterActivity.class);
//                intent.putExtra("name",centersName.get(position));
//                intent.putExtra("number",centersVaccines.get(position));
//                startActivity(intent);

                if(ContextCompat.checkSelfPermission(ListsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ListsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null) {
                        Intent intent = new Intent(ListsActivity.this, MyListsMapsActivity.class);
                        intent.putExtra("centersLatitude",latitude.get(position));
                        intent.putExtra("centersLongitude",longitude.get(position));

                        intent.putExtra("usersLatitude",lastKnownLocation.getLatitude());
                        intent.putExtra("usersLongitude",lastKnownLocation.getLongitude());

                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void updateListView(Location location)
    {
        if(location != null)
        {
            ParseQuery<ParseObject> query = null;
            System.out.println(code + " Code");
            if(code == 1)
            {
                query = new ParseQuery<ParseObject>("Volunteers");
            }

            else if(code == 2)
            {
                query = new ParseQuery<ParseObject>("Police");
                System.out.println("2 mai ghus gaya");
            }

            ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            query.whereNear("Location",parseGeoPoint);
            query.setLimit(10);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null)
                    {
                        name.clear();
                        latitude.clear();
                        longitude.clear();

                        if(objects.size() > 0)
                        {
                            List<Map<String, String>> listData = new ArrayList<>();
                            for(ParseObject object : objects)
                            {
                                ParseGeoPoint centersLocation = (ParseGeoPoint) object.get("Location");
//                                System.out.println(centersLocation);
                                if(centersLocation != null)
                                {
                                    double distanceInKms = parseGeoPoint.distanceInKilometersTo(centersLocation);
                                    double distanceAccuracy = (double) Math.round(distanceInKms * 10) / 10;

                                    if(distanceAccuracy < 10.0)
                                    {
                                        name.add(object.get("Name").toString());
                                        System.out.println("Name " + object.get("Name").toString());
                                        latitude.add(centersLocation.getLatitude());
                                        longitude.add(centersLocation.getLongitude());

                                        String message = "";
                                        if(code == 1)
                                        {
                                            message = distanceAccuracy + " KMS away from you.";
                                        }

                                        else if(code == 2)
                                        {
                                            message = object.get("Address").toString() + ", " + object.get("City").toString() + ", " + object.get("State").toString() + "\n" + distanceAccuracy + " KMS away from you.";
                                        }

                                        Map<String, String> centersInfo = new HashMap<>();
                                        centersInfo.put("name", object.get("Name").toString());
                                        centersInfo.put("message", message);
                                        listData.add(centersInfo);
                                    }
                                }
                            }

                            simpleAdapter = new SimpleAdapter(ListsActivity.this, listData, android.R.layout.simple_list_item_2, new String[] {"name","message"},new int[] {android.R.id.text1, android.R.id.text2}){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getView(position, convertView, parent);

                                    TextView one = view.findViewById(android.R.id.text1);
                                    TextView two = view.findViewById(android.R.id.text2);

                                    one.setTypeface(null, Typeface.BOLD_ITALIC);

                                    one.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                                    two.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                                    return view;
                                }
                            };

                            myListView.setAdapter(simpleAdapter);
                        }
                    }
                }
            });
        }
    }
}