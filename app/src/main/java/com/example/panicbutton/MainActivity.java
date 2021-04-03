package com.example.panicbutton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    EditText editTextUserName;
    EditText editTextUserEmail;
    EditText editTextPassword;
    EditText editTextConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        editTextUserName = findViewById(R.id.editTextUsername);
        editTextUserEmail = findViewById(R.id.editTextUserEmail);
        editTextPassword = findViewById(R.id.editTextUserPassword);
        editTextConfirm = findViewById(R.id.editTextUserPasswordConfirm);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void SignUp(View view)
    {
        String username = editTextUserName.getText().toString();
        String email = editTextUserEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirm = editTextConfirm.getText().toString();

        if(username.isEmpty())
        {
            editTextUserName.setError("Please enter a Username");
            return;
        }

        if(email.isEmpty())
        {
            editTextUserEmail.setError("Please enter your Email");
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Please enter your Password");
            return;
        }

        if(confirm.isEmpty())
        {
            editTextConfirm.setError("Please enter your confirmation password");
            return;
        }

        if(!password.equals(confirm))
        {
            editTextPassword.setError("Both Passwords should match");
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null)
                {
                    ParseUser.logOut();

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Account Created Successfully!")
                            .setMessage("Please verify your Email before Login!")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
//                                    Toast.makeText(MainActivity.this,"Closed",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }

                else
                {
                    ParseUser.logOut();

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error Account Creation Failed")
                            .setMessage("Account Could not be Created : " + e.getMessage())
                            .setNeutralButton("Close",null)
                            .show();
                }

            }
        });

    }

    public void GoToLoginActivity(View view)
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}