package com.mca.vnkyv.mytribe.Student.Authentication;

public class User {
    public String email, firstName, lastName, username, dob, course;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName, String username, String dob, String course) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.dob = dob;
        this.course = course;
    }
}
