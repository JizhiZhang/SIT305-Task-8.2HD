package com.example.imagepro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText usernameEditText = findViewById(R.id.loginEmailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button signupButton = findViewById(R.id.signupButton);
        db = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = db.fetchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                if (result >= 0) {
                    Toast.makeText(MainActivity.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(MainActivity.this, com.example.imagepro.HomeActivity.class);
//                    homeIntent.putExtra("CURRENT_USER", result);
//                    startActivityForResult(homeIntent, 0);
                    // 另一种尝试
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("CURRENT_USER", result);
                    editor.apply();
                    startActivity(homeIntent);
                    passwordEditText.setText("");
                    usernameEditText.setText("");
//                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "The user dose not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(MainActivity.this, com.example.imagepro.SignupActivity.class);
                startActivity(signupIntent);
            }
        });

    }
}