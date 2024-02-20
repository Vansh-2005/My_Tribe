package com.mca.vnkyv.mytribe.Student.AddButton.Project;

public class Project {
    private String title;
    private String description;
    private String imageUri;
    private String uid;
    private String creatorName;
    private String course;
    private String year;
    private String creatorEmail;
    private String commNote;
    private String startDate;
    private String endDate;
    private String status;

    // Default constructor required for calls to DataSnapshot.getValue(Community.class)
    public Project() {
        // Default constructor with no arguments
    }

    public Project(String title, String description, String imageUri, String uid, String creatorName, String course, String year, String creatorEmail, String commNote, String startDate, String endDate, String status) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.uid = uid;
        this.creatorName = creatorName;
        this.course = course;
        this.year = year;
        this.creatorEmail = creatorEmail;
        this.commNote = commNote;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and setters...

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCommNote() {
        return commNote;
    }

    public void setCommNote(String commNote) {
        this.commNote = commNote;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
