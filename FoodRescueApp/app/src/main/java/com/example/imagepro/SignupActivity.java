package com.example.imagepro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.model.User;

public class SignupActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText fullNameEditText = findViewById(R.id.fullNameEditText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText passwordEditText = findViewById(R.id.spasswordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button saveButton = findViewById(R.id.saveButton);
        db = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (fullNameEditText.getText().toString().equals("") || emailEditText.getText().toString().equals("") ||
                        phoneEditText.getText().toString().equals("") || addressEditText.getText().toString().equals("") ||
                        passwordEditText.getText().toString().equals("") || confirmPasswordEditText.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, "Please enter all information!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        long result = db.insertUser(new User(name, email, phone, address, password));
                        if (result > 0) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "Two passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}