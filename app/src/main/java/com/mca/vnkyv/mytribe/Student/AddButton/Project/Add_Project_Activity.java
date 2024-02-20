package com.mca.vnkyv.mytribe.Student.AddButton.Project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.AddButton.Event.Add_Event_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Event.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Add_Project_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView addProjectImg;
    private EditText projectTitleText;
    private EditText projectDescription;
    private EditText creatorName;
    private EditText courseName;
    private EditText courseYear;
    private EditText creatorEmailId;
    private EditText projectNote;
    private Button submitProject;
    private ProgressBar progressBar;

    private Uri imageUri;

    private EditText startDateEditText;
    private EditText endDateEditText;

    private Calendar startDateCalendar;
    private Calendar endDateCalendar;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Event");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c2f49")));

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.profile_notificationbar_color));


        addProjectImg = findViewById(R.id.add_project_img);
        projectTitleText = findViewById(R.id.project_title_text);
        projectDescription = findViewById(R.id.project_description);
        creatorName = findViewById(R.id.project_creator_name);
        courseName = findViewById(R.id.course_name);
        courseYear = findViewById(R.id.course_year);
        creatorEmailId = findViewById(R.id.creator_email_id);
        projectNote = findViewById(R.id.project_note);
        submitProject = findViewById(R.id.submit_project);
        progressBar = findViewById(R.id.progressBar);

        startDateEditText = findViewById(R.id.start_date_edittext);
        endDateEditText = findViewById(R.id.end_date_edittext);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("project_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("New project");
        auth = FirebaseAuth.getInstance();


        addProjectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submitProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProjectData();
            }
        });

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(startDateCalendar, startDateEditText);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(endDateCalendar, endDateEditText);
            }
        });

        // Schedule the task to update project statuses automatically
        scheduleStatusUpdateTask();
    }

    private void showDatePicker(final Calendar calendar, final EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateEditText(editText, calendar);
            }
        };

        new DatePickerDialog(Add_Project_Activity.this, dateSetListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateEditText(EditText editText, Calendar calendar) {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }


    private void openFileChooser() {
        // Implement code to choose an image from the device
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadProjectData() {
        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            UploadTask uploadTask = fileReference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        addDataToDatabase(downloadUri.toString());
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(Add_Project_Activity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDataToDatabase(String imageUrl) {
        String title = projectTitleText.getText().toString().trim();
        String description = projectDescription.getText().toString().trim();
        String name = creatorName.getText().toString().trim();
        String course = courseName.getText().toString().trim();
        String year = courseYear.getText().toString().trim();
        String email = creatorEmailId.getText().toString().trim();
        String note = projectNote.getText().toString().trim();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        String status = "";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date currentDate = new Date();
        try {
            Date endDateDate = sdf.parse(endDate);
            if (currentDate.before(endDateDate)) {
                status = "ongoing";
            } else {
                status = "finished";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing date", Toast.LENGTH_SHORT).show();
            return; // Exit the method if there's an error parsing the date
        }

        String userId = auth.getCurrentUser().getUid();

        String projectId = databaseReference.push().getKey();
        Project project = new Project(title, description, imageUrl, userId, name, course, year, email, note, startDate, endDate, status);

        databaseReference.child(projectId)
                .setValue(project)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Add_Project_Activity.this, "Project added successfully", Toast.LENGTH_SHORT).show();
                        updateUsersCommunities(userId, projectId);
                        finish();
                    } else {
                        Toast.makeText(Add_Project_Activity.this, "Error adding community", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUsersCommunities(String userId, String communityId) {
        // Assuming you have a "users" node in your database
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

        // Add the communityId to the user's profile
        usersReference.child(userId).child("project").child(communityId).setValue(communityId);
    }

    private String getFileExtension(Uri uri) {
        // Get the file extension from the content type of the file

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            // Use Glide to load and display the selected image
            Glide.with(this)
                    .load(imageUri)
                    .into(addProjectImg);
        }
    }

    // Method to periodically check and update project statuses
// Method to periodically check and update project statuses
    private void scheduleStatusUpdateTask() {
        Timer timer = new Timer();

        // Schedule a task to run every day
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Retrieve all projects from the database
                List<Project> projects = getAllProjectsFromDatabase();

                // Get the current date
                Date currentDate = new Date();

                // Define the date format
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

                // Iterate through each project
                for (Project project : projects) {
                    String endDateStr = project.getEndDate();
                    try {
                        // Parse the end date String into a Date object
                        Date endDate = sdf.parse(endDateStr);

                        // Check if the current date is after the end date
                        if (currentDate.after(endDate) && !project.getStatus().equals("finished")) {
                            // Update the status to "finished"
                            project.setStatus("finished");

                            // Update the project in the database
                            updateProjectInDatabase(project);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // Handle the ParseException if necessary
                    }
                }
            }
        }, 0, 24 * 60 * 60 * 1000); // Run the task every 24 hours (24 * 60 * 60 * 1000 milliseconds)
    }

    // Method to retrieve all projects from the database (implement this according to your database structure)
    private List<Project> getAllProjectsFromDatabase() {
        final List<Project> projectsList = new ArrayList<>();

        // Assuming you have a reference to your projects node in Firebase
        DatabaseReference projectsRef = FirebaseDatabase.getInstance().getReference("projects");

        // Attach a listener to retrieve the projects
        projectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list to avoid duplicates
                projectsList.clear();

                // Iterate through the children (projects) in the dataSnapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the project object and add it to the list
                    Project project = snapshot.getValue(Project.class);
                    projectsList.add(project);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur
                Log.e(TAG, "Error retrieving projects: " + databaseError.getMessage());
            }
        });

        // Return the list of projects
        return projectsList;
    }


    // Method to update a project in the database (implement this according to your database structure)
    private void updateProjectInDatabase(Project project) {
        // Get the project ID
        String projectId = project.getUid();

        // Reference to the project node in Firebase
        DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference("New project").child(projectId);

        // Update the project in the database
        projectRef.setValue(project)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Project updated successfully
                            Log.d(TAG, "Project updated successfully");
                        } else {
                            // Failed to update project
                            Log.e(TAG, "Failed to update project: " + task.getException().getMessage());
                        }
                    }
                });
    }

}
