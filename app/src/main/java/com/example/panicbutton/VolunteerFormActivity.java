package com.example.panicbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class VolunteerFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner genderSpinner;
    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_form);

        getSupportActionBar().hide();

        genderSpinner = findViewById(R.id.genderDropDownVol);
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.genders, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
//        selectedGender = gender;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}