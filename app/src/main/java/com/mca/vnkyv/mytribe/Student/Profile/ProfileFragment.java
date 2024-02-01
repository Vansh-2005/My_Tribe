package com.mca.vnkyv.mytribe.Student.Profile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mca.vnkyv.mytribe.R;

public class ProfileFragment extends Fragment {
    private TextView usernameTextView,fullNameTextView,emailTextView,dobTextView,community_countTextView
            ,event_countTextView,project_countTextView;

    private DatabaseReference userReference;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2c2f49"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("My Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);


        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.profile_notificationbar_color));

        usernameTextView = view.findViewById(R.id.username);
        fullNameTextView = view.findViewById(R.id.fullname);
        emailTextView = view.findViewById(R.id.email);
        dobTextView = view.findViewById(R.id.dob);
        community_countTextView = view.findViewById(R.id.community_count);
        event_countTextView = view.findViewById(R.id.total_event_count);
        project_countTextView = view.findViewById(R.id.total_project_count);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve specific fields from the database
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);

                        // Retrieve the "community" node
                        DataSnapshot communitySnapshot = dataSnapshot.child("community");
                        DataSnapshot eventSnapshot = dataSnapshot.child("event");
                        DataSnapshot projectSnapshot = dataSnapshot.child("project");

                        // Calculate the count of communities
                        long communityCount = communitySnapshot.getChildrenCount();
                        long eventCount = eventSnapshot.getChildrenCount();
                        long projectCount = projectSnapshot.getChildrenCount();

                        // Combine first name and last name to get full name
                        String fullName = firstName + " " + lastName;

                        // Update UI with retrieved data
                        usernameTextView.setText(username);
                        fullNameTextView.setText(fullName);
                        emailTextView.setText(email);
                        dobTextView.setText(dob);
                        community_countTextView.setText(String.valueOf(communityCount)); // Convert count to String
                        event_countTextView.setText(String.valueOf(eventCount)); // Convert count to String
                        project_countTextView.setText(String.valueOf(projectCount)); // Convert count to String
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                }
            });
        }



        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.student_dots_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.student_logout) {
            Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_add) {
            Toast.makeText(getActivity(), "Notification", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}