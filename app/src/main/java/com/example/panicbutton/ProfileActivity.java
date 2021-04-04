package com.example.panicbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextInputEditText username;
    TextInputEditText userEmail;
    TextInputEditText userPhone;
    TextInputEditText userAge;
    TextView profileName;
    TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.inputUsername);
        userEmail = findViewById(R.id.inputUserEmail);
        userPhone = findViewById(R.id.inputUserAddress);
        userAge = findViewById(R.id.inputUserAge);
        profileName = findViewById(R.id.profile_name);
        gender = findViewById(R.id.gender);

        getSupportActionBar().hide();

        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        username.setText(object.get("FullName").toString() + " " + object.get("LastName").toString());
                        userEmail.setText(object.get("email").toString());
                        userPhone.setText(object.get("Address").toString());
                        userAge.setText(object.get("Age").toString());
                        profileName.setText(object.get("username").toString());
                        gender.setText(object.get("Gender").toString());
                    }
                }
            }
        });
    }

    public void updateProfile(View view)
    {
        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        System.out.println(userAge.getText().toString());
                        object.put("FullName",username.getText().toString());
                        object.put("LastName",object.get("LastName").toString());
                        object.put("email",userEmail.getText().toString());
                        object.put("Address",userPhone.getText().toString());
                        object.put("Age",userAge.getText().toString());
                        object.saveInBackground();
                    }
                }
            }
        });

    }
}