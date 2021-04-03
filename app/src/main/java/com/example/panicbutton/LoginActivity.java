package com.example.panicbutton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUserNameLogin;
    EditText editTextUserPasswordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        editTextUserNameLogin = findViewById(R.id.editTextUsernameLogin);
        editTextUserPasswordLogin = findViewById(R.id.editTextUserPasswordLogin);

    }

    public void Login(View view)
    {
        String username = editTextUserNameLogin.getText().toString();
        String password = editTextUserPasswordLogin.getText().toString();

        if(username.isEmpty())
        {
            editTextUserNameLogin.setError("Please enter a Username");
            return;
        }

        if(password.isEmpty())
        {
            editTextUserPasswordLogin.setError("Please enter your Password");
            return;
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null)
                {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Successful!")
                            .setMessage("Welcome " + username)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
                else
                {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Failed")
                            .setMessage(e.getMessage())
                            .setNeutralButton("Close",null)
                            .show();
                }
            }
        });

    }
}