package com.mca.vnkyv.mytribe.Student.Authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mca.vnkyv.mytribe.R;

public class Register_Activity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button register_btn;
    private TextInputEditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, usernameEditText, dobEditText, courseEditText;

    // Firebase Realtime Database
    private DatabaseReference databaseReference;
    private TextView loginTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();

        ColorDrawable colorDrawable  = new ColorDrawable(Color.parseColor("#671935"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("User Registration");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.register_notificationbar_color));

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users"); // "users" is the name of the node in the database

        firstNameEditText = findViewById(R.id.firstname);
        lastNameEditText = findViewById(R.id.lastname);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        usernameEditText = findViewById(R.id.username);
        dobEditText = findViewById(R.id.dob);
        courseEditText = findViewById(R.id.course);
        register_btn = findViewById(R.id.register_btn);
        loginTextView = findViewById(R.id.login_text);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Link to Login Activity

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                 startActivity(intent);
                 finish();
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String course = courseEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || username.isEmpty() || dob.isEmpty() || course.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = auth.getCurrentUser();

                        // Store additional details in Firebase Realtime Database
                        saveUserDetails(user.getUid(), email, firstName, lastName, username, dob, course);

                        // Handle your successful registration here
                        Toast.makeText(Register_Activity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(Register_Activity.this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDetails(String userId, String email, String firstName, String lastName, String username, String dob, String course) {
        // Create a user object with the details
        User user = new User(email, firstName, lastName, username, dob, course);

        // Store the user object in the database under the user's UID
        databaseReference.child(userId).setValue(user);
    }
}
