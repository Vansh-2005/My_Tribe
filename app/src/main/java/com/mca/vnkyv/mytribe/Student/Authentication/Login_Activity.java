package com.mca.vnkyv.mytribe.Student.Authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.mca.vnkyv.mytribe.MainActivity;
import com.mca.vnkyv.mytribe.R;

public class Login_Activity extends AppCompatActivity {

    private MaterialCardView student_pannel,admin_pannel;
    private TextView fgpassword , newAccount,continue_btn;
    private Button Admin_login,Student_login,Admin_continue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.login_notificationbar_color2));

        continue_btn = findViewById(R.id.login);
        student_pannel = findViewById(R.id.student_pannel);
        Admin_login = (Button)findViewById(R.id.admin_button);
        admin_pannel = findViewById(R.id.admin_pannel);
        Student_login = findViewById(R.id.Student_login);
        Admin_continue =findViewById(R.id.admin_continue);

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(Login_Activity.this, "Login Succesfully", Toast.LENGTH_SHORT).show();
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
    }}
