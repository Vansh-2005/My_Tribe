package com.mca.vnkyv.mytribe.Student.Splash_Sceen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.mca.vnkyv.mytribe.MainActivity;
import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.Authentication.Login_Activity;

public class Splash_Screen extends AppCompatActivity {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.splash_notificationbar_color));


        new Handler().postDelayed(() -> {
            // Check if the user is logged in
            boolean userLoggedIn = isLoggedIn();

            Intent intent;
            if (userLoggedIn) {
                intent = new Intent(Splash_Screen.this, MainActivity.class);
            } else {
                intent = new Intent(Splash_Screen.this, Login_Activity.class);
            }

            startActivity(intent);
            finish();
        }, 2000);
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

}