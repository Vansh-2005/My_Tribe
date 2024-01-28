package com.mca.vnkyv.mytribe.Student.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mca.vnkyv.mytribe.MainActivity;
import com.mca.vnkyv.mytribe.R;

public class Login_Activity extends AppCompatActivity {

    private MaterialCardView student_pannel, admin_pannel;
    private TextView fgpassword, newAccount, continue_btn;
    private Button Admin_login, Student_login, Admin_continue;
    private TextView click_here_forgot, create_new_account;

    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
//    private ProgressBar progressBar;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.login_notificationbar_color2));

        continue_btn = findViewById(R.id.login_btn);
        student_pannel = findViewById(R.id.student_pannel);
        Admin_login = (Button) findViewById(R.id.admin_button);
        admin_pannel = findViewById(R.id.admin_pannel);
        Student_login = findViewById(R.id.Student_login);
        Admin_continue = findViewById(R.id.admin_continue);
        click_here_forgot = findViewById(R.id.click_here_forgot);
        create_new_account = findViewById(R.id.create_new_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
//        progressBar=findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();


        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Useremail = email.getText().toString().trim();
                String Userpassword = password.getText().toString().trim();
                if (!Useremail.isEmpty() && !Userpassword.isEmpty()) {

                    continue_btn.setEnabled(false);
//                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(Useremail, Userpassword)
                            .addOnCompleteListener(Login_Activity.this, task -> {
                                continue_btn.setEnabled(true);
//                                progressBar.setVisibility(View.VISIBLE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    updateLoginStatus(true);
                                    Toast.makeText(Login_Activity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login_Activity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(Login_Activity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_pannel.setVisibility(View.GONE);
                admin_pannel.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        Student_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_pannel.setVisibility(View.VISIBLE);
                admin_pannel.setVisibility(View.GONE);
//                Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
//        Admin_continue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Login_Activity.this, AdminDashboard.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        click_here_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Forgot_Password_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateLoginStatus(boolean isLoggedIn) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
}
