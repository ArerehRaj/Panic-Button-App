package com.example.panicbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class AddDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner genderSpinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    String selectedGender = null;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextAge;
    EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        getSupportActionBar().hide();

        editTextFirstName = findViewById(R.id.editTextPersonFirstName);
        editTextLastName = findViewById(R.id.editTextPersonLastName);
        editTextAge = findViewById(R.id.editTextNumberAge);
        editTextAddress = findViewById(R.id.editTextMultiLineAddress);

        genderSpinner = findViewById(R.id.genderDropDown);
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.genders, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
        selectedGender = gender;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void SaveDetails(View view)
    {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String age = editTextAge.getText().toString();
        String address = editTextAddress.getText().toString();

        if(firstName.isEmpty())
        {
            editTextFirstName.setError("Please Enter Your First Name.");
            return;
        }

        if(lastName.isEmpty())
        {
            editTextFirstName.setError("Please Enter Your Last Name.");
            return;
        }

        if(age.isEmpty())
        {
            editTextFirstName.setError("Please Enter Your Age.");
            return;
        }

        if(address.isEmpty())
        {
            editTextFirstName.setError("Please Enter Your Address.");
            return;
        }

        ParseQuery<ParseUser> user = ParseUser.getQuery();
        user.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());

        user.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null)
                {
                    for(ParseObject object : objects)
                    {
                        object.put("FullName",firstName);
                        object.put("LastName",lastName);
                        object.put("Gender",selectedGender);
                        object.put("Age",Integer.parseInt(age));
                        object.put("Address",address);
                        object.put("IsDataGiven",true);
                        object.saveInBackground();
//                        Toast.makeText(AddDetailsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(AddDetailsActivity.this, HomeActivity.class);
                        startActivity(newIntent);
                    }
                }
            }
        });
    }
}