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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.mca.vnkyv.mytribe.R;

public class Forgot_Password_Activity extends AppCompatActivity {

    private TextInputEditText forgotEmail;
    private Button forgotLinkButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        forgotEmail = findViewById(R.id.forgot_email);
        forgotLinkButton = findViewById(R.id.Forgot_Button);
        progressBar = findViewById(R.id.progressBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Forgot Password");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#671935")));
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.fgpassword_notificationbar_color));

        forgotLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = forgotEmail.getText().toString().trim();

                if (!userEmail.isEmpty()) {
                    // Disable the button and show the progress bar
                    forgotLinkButton.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    forgotEmail.setText("");

                    // Send reset password link
                    mAuth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(task -> {
                                // Enable the button and hide the progress bar when the task is complete
                                forgotLinkButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Email sent successfully
                                    Toast.makeText(Forgot_Password_Activity.this,
                                            "Password reset link sent to your email",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the email is not registered or other error occurred
                                    Toast.makeText(Forgot_Password_Activity.this,
                                            "Failed to send reset link. Please check your email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Show a message to enter an email address
                    Toast.makeText(Forgot_Password_Activity.this,
                            "Please enter your registered email address.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
