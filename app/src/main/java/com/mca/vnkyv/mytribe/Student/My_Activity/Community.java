package com.mca.vnkyv.mytribe.Student.My_Activity;

public class Community {
    private String commNote;
    private String course;
    private String creatorEmail;
    private String creatorName;
    private String description;
    private String imageUri;
    private String title;
    private String uid;
    private String year;

    // Default constructor
    public Community() {
        // Default constructor required for Firebase
    }

    // Parameterized constructor
    public Community(String commNote, String course, String creatorEmail, String creatorName, String description, String imageUri, String title, String uid, String year) {
        this.commNote = commNote;
        this.course = course;
        this.creatorEmail = creatorEmail;
        this.creatorName = creatorName;
        this.description = description;
        this.imageUri = imageUri;
        this.title = title;
        this.uid = uid;
        this.year = year;
    }

    // Getters and setters

    public String getCommNote() {
        return commNote;
    }

    public void setCommNote(String commNote) {
        this.commNote = commNote;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
