package com.mca.vnkyv.mytribe.Student.About_Us;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mca.vnkyv.mytribe.R;

public class About_Us_Activity extends AppCompatActivity {

    private WebView aboutUs_webview;
    private ProgressBar progressBar;

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutUs_webview = findViewById(R.id.about_us_webview);
        progressBar = findViewById(R.id.about_progress_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("About Us");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c2f49")));

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.profile_notificationbar_color));

        // Get the Firebase database reference
        DatabaseReference aboutUsRef = FirebaseDatabase.getInstance().getReference("AboutUs");

        // Fetch the URL from the database
        aboutUsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String aboutUsUrl = dataSnapshot.getValue(String.class);

                    // Load the fetched URL into the WebView
                    if (aboutUsUrl != null && !aboutUsUrl.isEmpty()) {
                        loadWebView(aboutUsUrl);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView(String url) {
        WebSettings webSettings = aboutUs_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        aboutUs_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Page has finished loading, hide the progress bar and show the WebView
                progressBar.setVisibility(ProgressBar.GONE);
                aboutUs_webview.setVisibility(WebView.VISIBLE);
            }
        });

        // Show the progress bar and hide the WebView while loading
        progressBar.setVisibility(ProgressBar.VISIBLE);
        aboutUs_webview.setVisibility(WebView.GONE);

        aboutUs_webview.loadUrl(url);
    }
}
