package com.mca.vnkyv.mytribe.Student.AddButton.Community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.mca.vnkyv.mytribe.Student.AddButton.Community.Community;

public class Add_Community_Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView addComImg;
    private EditText comTitleText;
    private EditText comDescription;
    private EditText creatorName;
    private EditText courseName;
    private EditText courseYear;
    private EditText creatorEmailId;
    private EditText commNote;
    private Button submitComm;
    private ProgressBar progressBar;

    private Uri imageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community);

        addComImg = findViewById(R.id.add_Com_img);
        comTitleText = findViewById(R.id.com_title_text);
        comDescription = findViewById(R.id.com_description);
        creatorName = findViewById(R.id.creator_name);
        courseName = findViewById(R.id.course_name);
        courseYear = findViewById(R.id.course_year);
        creatorEmailId = findViewById(R.id.creator_email_id);
        commNote = findViewById(R.id.comm_note);
        submitComm = findViewById(R.id.submit_comm);
        progressBar = findViewById(R.id.progressBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Community");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2c2f49")));

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.profile_notificationbar_color));


        storageReference = FirebaseStorage.getInstance().getReference("community_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("New community");
        auth = FirebaseAuth.getInstance();

        addComImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submitComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
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

    private void uploadData() {
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
                        Toast.makeText(Add_Community_Activity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDataToDatabase(String imageUrl) {
        String title = comTitleText.getText().toString().trim();
        String description = comDescription.getText().toString().trim();
        String name = creatorName.getText().toString().trim();
        String course = courseName.getText().toString().trim();
        String year = courseYear.getText().toString().trim();
        String email = creatorEmailId.getText().toString().trim();
        String note = commNote.getText().toString().trim();

        String userId = auth.getCurrentUser().getUid();

        String communityId = databaseReference.push().getKey();
        Community community = new Community(title, description, imageUrl, name, course, year, email, note, userId);

        databaseReference.child(communityId)
                .setValue(community)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Add_Community_Activity.this, "Community added successfully", Toast.LENGTH_SHORT).show();
                        updateUsersCommunities(userId, communityId);
                        finish();
                    } else {
                        Toast.makeText(Add_Community_Activity.this, "Error adding community", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUsersCommunities(String userId, String communityId) {
        // Assuming you have a "users" node in your database
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");

        // Add the communityId to the user's profile
        usersReference.child(userId).child("community").child(communityId).setValue(communityId);
    }

    private String getFileExtension(Uri uri) {
        // Implement code to get the file extension
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
                    .into(addComImg);
        }
    }
}

