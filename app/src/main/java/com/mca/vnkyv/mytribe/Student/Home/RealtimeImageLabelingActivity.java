package com.mca.vnkyv.mytribe.Student.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.mca.vnkyv.mytribe.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RealtimeImageLabelingActivity extends AppCompatActivity {

    private Button realTimeImageButton;
    private Button galleryImageButton;
    private ImageView selectedImageView;
    private TextView resultTextView;
    private ProcessCameraProvider cameraProvider;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 1001;
    private static final int IMAGE_ANALYSIS_THREAD_POOL_SIZE = 4;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1002;

    private ExecutorService cameraExecutor;
    private ImageLabeler labeler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_image_labeling);

        // Initialize views
        realTimeImageButton = findViewById(R.id.realTimeImageButton);
        galleryImageButton = findViewById(R.id.galleryImageButton);
        selectedImageView = findViewById(R.id.selectedImageView);
        resultTextView = findViewById(R.id.resultTextView);

        // Initialize camera executor
        cameraExecutor = Executors.newFixedThreadPool(IMAGE_ANALYSIS_THREAD_POOL_SIZE);

        // Initialize ImageLabeler
        labeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder().build());

        // Set click listeners
        realTimeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermissionAndStartRealTimeCapture();
            }
        });

        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryImageSelection();
            }
        });
    }

    private void requestCameraPermissionAndStartRealTimeCapture() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
           // startRealTimeImageCapture();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE
            );
        }
    }

//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode,
//            @NonNull String[] permissions,
//            @NonNull int[] grantResults
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE &&
//                grantResults.length > 0 &&
//                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            startRealTimeImageCapture();
//        }
//    }

//    private void startRealTimeImageCapture() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
//                ProcessCameraProvider.getInstance(this);
//
//        cameraProviderFuture.addListener(() -> {
//            try {
//                // Retrieve the ProcessCameraProvider instance
//                cameraProvider = cameraProviderFuture.get();
//
//                if (cameraProvider != null) {
//                    Camera camera = cameraProvider.bindToLifecycle(
//                            (LifecycleOwner) this,
//                            getCameraSelector(),
//                            getPreviewUseCase(),
//                            getImageAnalysisUseCase()
//                    );
//
//                    // Additional CameraX configuration if needed
//
//                } else {
//                    // Handle camera initialization failure
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    private CameraSelector getCameraSelector() {
//        return new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//    }
//    private Preview getPreviewUseCase() {
//        Preview preview = new Preview.Builder().build();
//        preview.setSurfaceProvider(
//                selectedImageView.createSurfaceProvider());
//        return preview;
//    }
//    private ImageAnalysis getImageAnalysisUseCase() {
//        ImageAnalysis imageAnalysis =
//                new ImageAnalysis.Builder()
//                        .setTargetResolution(new Size(640, 480))
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build();
//
//        imageAnalysis.setAnalyzer(cameraExecutor, new YourAnalyzer());
//
//        return imageAnalysis;
//    }


    private void startGalleryImageSelection() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the result from the image selection
            Uri selectedImageUri = data.getData();

            // Set the selected image to the ImageView
            selectedImageView.setImageURI(selectedImageUri);

            // Perform image labeling
            performImageLabeling(selectedImageUri);
        }
    }

    private void performImageLabeling(Uri imageUri) {
        try {
            InputImage image = InputImage.fromFilePath(getApplicationContext(), imageUri);

            // Set the desired options for ImageLabeler
            ImageLabelerOptions options =
                    new ImageLabelerOptions.Builder()
                            .build();

            // Create an ImageLabeler instance
            ImageLabeler labeler = ImageLabeling.getClient(options);

            labeler.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                        @Override
                        public void onSuccess(List<ImageLabel> labels) {
                            // Handle the image labeling results
                            StringBuilder resultText = new StringBuilder();

                            for (ImageLabel label : labels) {
                                String labelText = label.getText();

                                // Append the label name to the resultText
                                resultText.append(labelText).append("\n");
                            }

                            // Display the result in resultTextView
                            resultTextView.setText(resultText.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors during image labeling
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
