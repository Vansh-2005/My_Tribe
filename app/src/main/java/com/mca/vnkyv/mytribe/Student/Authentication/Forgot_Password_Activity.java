package com.mca.vnkyv.mytribe.Student.Authentication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mca.vnkyv.mytribe.R;

public class Forgot_Password_Activity extends AppCompatActivity {

    private Button ForgotLinkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ForgotLinkButton = findViewById(R.id.ForgotLinkButton);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Forgot Password");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#671935"));
        actionBar.setBackgroundDrawable(colorDrawable);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.fgpassword_notificationbar_color));


    }
}