package com.mca.vnkyv.mytribe.Student.My_Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mca.vnkyv.mytribe.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private DatabaseReference projectsReference;
    private DatabaseReference communitiesReference;
    private DatabaseReference eventsReference;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2c2f49"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("My Activity");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.profile_notificationbar_color));

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            projectsReference = FirebaseDatabase.getInstance().getReference("New project");
            communitiesReference = FirebaseDatabase.getInstance().getReference("New community");
            eventsReference = FirebaseDatabase.getInstance().getReference("New event");
        }

        RecyclerView recyclerViewProjects = view.findViewById(R.id.project_recyclerView);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        ProjectAdapter projectAdapter = new ProjectAdapter(requireContext(), new ArrayList<>());
        recyclerViewProjects.setAdapter(projectAdapter);

         RecyclerView recyclerViewCommunities = view.findViewById(R.id.community_recyclerView);
        recyclerViewCommunities.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        CommunityAdapter communityAdapter = new CommunityAdapter(requireContext(), new ArrayList<>());
        recyclerViewCommunities.setAdapter(communityAdapter);

       RecyclerView recyclerViewEvents = view.findViewById(R.id.Event_recyclerView);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
        EventAdapter eventAdapter = new EventAdapter(requireContext(), new ArrayList<>());
        recyclerViewEvents.setAdapter(eventAdapter);

        // Retrieve Projects and update the adapter
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Call the methods with the userId
            retrieveProjects(projectAdapter, userId);
            retrieveCommunities(communityAdapter, userId);
            retrieveEvents(eventAdapter, userId);
        }


        return view;
    }

    private void retrieveProjects(ProjectAdapter adapter, String userId) {
        projectsReference.orderByChild("commNote").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Project> projectList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project project = snapshot.getValue(Project.class);
                    if (project != null) {
                        projectList.add(project);
                    }
                }
                adapter.setProjectList(projectList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ActivityFragment", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void retrieveCommunities(CommunityAdapter adapter, String userId) {
        communitiesReference.orderByChild("commNote").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Community> communityList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Community community = snapshot.getValue(Community.class);
                    if (community != null) {
                        communityList.add(community);
                    }
                }
                adapter.setCommunityList(communityList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ActivityFragment", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void retrieveEvents(EventAdapter adapter, String userId) {
        eventsReference.orderByChild("commNote").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                adapter.setEventList(eventList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ActivityFragment", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
