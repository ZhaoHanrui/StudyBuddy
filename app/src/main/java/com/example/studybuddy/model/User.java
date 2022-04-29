package com.example.studybuddy.model;

import android.widget.ImageView;

public class User {
    private String firstName;
    private String lastName;
    private String gender;
    private String year;
    private String faculty;
    private String email;
    private String password;
    private ImageView profilePicture;

    public User(String firstName, String lastName, String gender, String year,
                String faculty, String email, String password, ImageView profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.year = year;
        this.faculty = faculty;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public void setFirstName(String name) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String name) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setProfilePicture(ImageView profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ImageView getProfilePicture() {
        return profilePicture;
    }

}
