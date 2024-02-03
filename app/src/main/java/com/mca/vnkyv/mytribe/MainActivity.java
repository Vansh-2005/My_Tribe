package com.mca.vnkyv.mytribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mca.vnkyv.mytribe.Student.About_Us.About_Us_Activity;
import com.mca.vnkyv.mytribe.Student.Developer_Contact.Developer_Contact_Activity;
import com.mca.vnkyv.mytribe.Student.Privacy_Policy.Privacy_Policy_Activity;
import com.mca.vnkyv.mytribe.Student.Terms_Condition.Terms_Condition_Activity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView headerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.frame_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.dashboard_item);


//        to frct header name and image
        headerUsername = navigationView.getHeaderView(0).findViewById(R.id.header_username);
        fetchAndSetUsername(headerUsername);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    public void onBackPressed() {


        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_home) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.dashboard_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(shareIntent, "Share " + getString(R.string.app_name)));

        }
        else if (itemId == R.id.dashboard_rate_us)
        {
            rateUs();
        }
        else if (itemId == R.id.dashboard_about_us)
        {
           Intent i = new Intent(MainActivity.this, About_Us_Activity.class);
           startActivity(i);
           finish();
        }
        else if (itemId == R.id.dashboard_privacy)
        {
            Intent i = new Intent(MainActivity.this, Privacy_Policy_Activity.class);
            startActivity(i);
            finish();
        }
        else if (itemId == R.id.dashboard_term)
        {
            Intent i = new Intent(MainActivity.this, Terms_Condition_Activity.class);
            startActivity(i);
            finish();
        }
        else if (itemId == R.id.dashboard_developer) {
            Intent i = new Intent(MainActivity.this, Developer_Contact_Activity.class);
            startActivity(i);
            finish();
        }

        return true;
    }

    private void rateUs() {
        String packageName = getPackageName();

        // Create an Intent to open your app's page on the Play Store
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        // To prevent crashes if the Play Store app is not installed
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            // If Play Store app is not available, open the Play Store website
            Uri webUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }


    private void fetchAndSetUsername(final TextView headerUsername) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        if (username != null) {
                            headerUsername.setText(username);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                }
            });
        }
    }

}