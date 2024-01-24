package com.mca.vnkyv.mytribe.Student.Splash_Sceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.Authentication.Login_Activity;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.splash_notificationbar_color));


        Handler handler = new Handler();
handler.postDelayed(new Runnable() {
    @Override
    public void run() {
        Intent intent = new Intent(Splash_Screen.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }
},2000);
    }
}