package com.mca.vnkyv.mytribe.Student.My_Activity;

public class Event {
    private String title;
    private String description;
    private String date;
    private String imageUri;

    // Default constructor required for Firebase
    public Event() {
    }

    // Constructor with parameters
    public Event(String title, String description, String date, String imageUri) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageUri = imageUri;
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
}
