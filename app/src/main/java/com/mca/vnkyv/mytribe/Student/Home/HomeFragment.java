package com.mca.vnkyv.mytribe.Student.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.AddButton.Community.Add_Community_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Event.Add_Event_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Project.Add_Project_Activity;
import com.mca.vnkyv.mytribe.Student.My_Activity.Community;
import com.mca.vnkyv.mytribe.Student.My_Activity.CommunityAdapter;
import com.mca.vnkyv.mytribe.Student.My_Activity.Event;
import com.mca.vnkyv.mytribe.Student.My_Activity.EventAdapter;
import com.mca.vnkyv.mytribe.Student.My_Activity.Project;
import com.mca.vnkyv.mytribe.Student.My_Activity.ProjectAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private ActionBar actionBar;
    private DatabaseReference projectsReference;
    private DatabaseReference communitiesReference;
    private DatabaseReference eventsReference;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    private SpeechRecognizer speechRecognizer;
    private final int REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        Drawable d = getResources().getDrawable(R.drawable.toolbar);
        actionBar.setBackgroundDrawable(d);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireActivity());

        if (currentUser != null) {
            projectsReference = FirebaseDatabase.getInstance().getReference("New project");
            communitiesReference = FirebaseDatabase.getInstance().getReference("New community");
            eventsReference = FirebaseDatabase.getInstance().getReference("New event");
        }

        RecyclerView recyclerViewProjects = view.findViewById(R.id.project_recyclerView1);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        ProjectAdapter projectAdapter = new ProjectAdapter(requireContext(), new ArrayList<>());
        recyclerViewProjects.setAdapter(projectAdapter);

        RecyclerView recyclerViewCommunities = view.findViewById(R.id.community_recyclerView1);
        recyclerViewCommunities.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        CommunityAdapter communityAdapter = new CommunityAdapter(requireContext(), new ArrayList<>());
        recyclerViewCommunities.setAdapter(communityAdapter);

        RecyclerView recyclerViewEvents = view.findViewById(R.id.Event_recyclerView1);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        EventAdapter eventAdapter = new EventAdapter(requireContext(), new ArrayList<>());
        recyclerViewEvents.setAdapter(eventAdapter);

        // Retrieve Projects and update the adapter
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Call the methods with the userId
            retrieveProjects(projectAdapter);
            retrieveCommunities(communityAdapter);
            retrieveEvents(eventAdapter);
        }

        // Set ActionBar title and window settings
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming that the user's first name is stored in the "firstName" field in the database
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    // Capitalize the first letter of the name
                    String capitalizedFirstName = capitalizeFirstLetter(firstName);
                    // Set the first name as the title in the ActionBar
                    actionBar.setTitle("Hey " + capitalizedFirstName + ",");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        });

        // Set window status bar color
        Window window = requireActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.home_notificationbar_color));

        return view;
    }

    private void retrieveProjects(ProjectAdapter adapter) {
        projectsReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
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

    private void retrieveCommunities(CommunityAdapter adapter) {
        communitiesReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
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

    private void retrieveEvents(EventAdapter adapter) {
        eventsReference.orderByChild("uid").addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_image) {
            Intent i = new Intent(getActivity(), RealtimeImageLabelingActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.voice_search) {
            startVoiceRecognition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = result.get(0);
                executeVoiceCommand(text);
            }
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    private void executeVoiceCommand(String text) {
        text = text.toLowerCase();

        if (text.contains("create project")) {
            Intent i = new Intent(getActivity(), Add_Project_Activity.class);
            startActivity(i);
            Toast.makeText(getActivity(), "Create your own project here!!", Toast.LENGTH_SHORT).show();
        } else if (text.contains("create event")) {
            Intent i = new Intent(getActivity(), Add_Event_Activity.class);
            startActivity(i);
            Toast.makeText(getActivity(), "Create your own event here!!", Toast.LENGTH_SHORT).show();
        } else if (text.contains("create community")) {
            Intent i = new Intent(getActivity(), Add_Community_Activity.class);
            startActivity(i);
            Toast.makeText(getActivity(), "Create your own community here!!", Toast.LENGTH_SHORT).show();
        }
        else if (text.contains("open google")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(intent);
        }
        else if (text.contains("profile")) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frame_layout);
            navController.navigate(R.id.navigation_profile);
        } else if (text.contains("my activity")) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.frame_layout);
            navController.navigate(R.id.navigation_activity);
        } else {
            // If the command is not recognized, show a message to the user
            Toast.makeText(getActivity(), "Command not recognized", Toast.LENGTH_SHORT).show();
        }
    }
}
