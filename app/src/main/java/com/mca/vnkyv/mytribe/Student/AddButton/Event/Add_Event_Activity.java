package com.mca.vnkyv.mytribe.Student.AddButton.Event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mca.vnkyv.mytribe.R;
import com.mca.vnkyv.mytribe.Student.AddButton.Community.Add_Community_Activity;
import com.mca.vnkyv.mytribe.Student.AddButton.Community.Community;

public class Add_Event_Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView addEventImg;
    private EditText eventTitleText;
    private EditText eventDescription;
    private EditText creatorName;
    private EditText courseName;
    private EditText courseYear;
    private EditText creatorEmailId;
    private EditText eventNote;
    private Button submitEvent;
    private ProgressBar progressBar;

    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Event");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c2f49")));

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.profile_notificationbar_color));


        addEventImg = findViewById(R.id.add_event_img);
        eventTitleText = findViewById(R.id.event_title_text);
        eventDescription = findViewById(R.id.event_description);
        creatorName = findViewById(R.id.event_creator_name);
        courseName = findViewById(R.id.course_name);
        courseYear = findViewById(R.id.course_year);
        creatorEmailId = findViewById(R.id.creator_email_id);
        eventNote = findViewById(R.id.event_note);
        submitEvent = findViewById(R.id.submit_event);
        progressBar = findViewById(R.id.progressBar);

        storageReference = FirebaseStorage.getInstance().getReference("event_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("New event");
        auth = FirebaseAuth.getInstance();

        addEventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEventData();
            }
        });
    }

    private void openFileChooser() {
        // Implement code to choose an image from the device
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadEventData() {
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
                        Toast.makeText(Add_Event_Activity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDataToDatabase(String imageUrl) {
        String title = eventTitleText.getText().toString().trim();
        String description = eventDescription.getText().toString().trim();
        String name = creatorName.getText().toString().trim();
        String course = courseName.getText().toString().trim();
        String year = courseYear.getText().toString().trim();
        String email = creatorEmailId.getText().toString().trim();
        String note = eventNote.getText().toString().trim();

        String userId = auth.getCurrentUser().getUid();

        String eventId = databaseReference.push().getKey();
        Event event = new Event(title, description, imageUrl, name, course, year, email, note, userId);

        databaseReference.child(eventId)
                .setValue(event)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Add_Event_Activity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        updateUsersCommunities(userId, eventId);
                        finish();
                    } else {
                        Toast.makeText(Add_Event_Activity.this, "Error adding community", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateUsersCommunities(String userId, String communityId) {
        // Assuming you have a "users" node in your database
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

        // Add the communityId to the user's profile
        usersReference.child(userId).child("event").child(communityId).setValue(communityId);
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
                    .into(addEventImg);
        }
    }

}