package com.mca.vnkyv.mytribe.Student.My_Activity;

public class Project {
    private String title;
    private String description;
    private String date;
    private String imageUri;
    private String creatorName;
    private String status;

    // Default constructor required for Firebase
    public Project() {
    }

    // Constructor with parameters
    public Project(String title, String description, String date, String imageUri, String creatorName, String status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageUri = imageUri;
        this.creatorName = creatorName;
        this.status = status;
    }

    // Getter and setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
