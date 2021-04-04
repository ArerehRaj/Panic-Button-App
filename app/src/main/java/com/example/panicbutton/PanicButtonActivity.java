package com.example.panicbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PanicButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);

        getSupportActionBar().hide();
    }
}