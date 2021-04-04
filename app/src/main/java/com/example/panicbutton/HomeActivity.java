package com.example.panicbutton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
    }

    public void logOut(View view){
        ParseUser.logOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void VolonteerForm(View view)
    {
        Intent intent = new Intent(HomeActivity.this, VolunteerFormActivity.class);
        startActivity(intent);
    }

    public void PromptUser(View view)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Title");
        alertDialog.setMessage("Message");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Volunteers",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, ListsActivity.class);
                        intent.putExtra("Code",1);
                        intent.putExtra("title","Volunteers Near you");
                        startActivity(intent);
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Police Station",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, ListsActivity.class);
                        intent.putExtra("Code",2);
                        intent.putExtra("title","Police Stations Near you");
                        startActivity(intent);
                    }
                });

        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }

    public void goSOS(View view)
    {
        Intent intent = new Intent(HomeActivity.this, PanicButtonActivity.class);
        startActivity(intent);
    }
}