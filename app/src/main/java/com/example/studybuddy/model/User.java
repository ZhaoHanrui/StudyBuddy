package com.example.studybuddy.model;

import java.util.HashSet;

public class User {
    private String name;
    private String gender;
    private String year;
    private String faculty;
    private String profilePicture = "";

    public User(String name, String gender, String year, String faculty, String profilePicture) {
        this.name = name;
        this.gender = gender;
        this.year = year;
        this.faculty = faculty;
        this.profilePicture = profilePicture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPronouns(String pronouns) {
        this.gender = gender;
    }

    public String getPronouns() {
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

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

}
