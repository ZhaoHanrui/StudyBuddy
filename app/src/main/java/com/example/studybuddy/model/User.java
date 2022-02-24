package com.example.studybuddy.model;

import java.util.HashSet;

public class User {
    private String name;
    private String gender;
    private int year;
    private String major;
    private HashSet<String> tags;
    private String profilePicture = "./";

    public User(String name, String gender, int year, String major, HashSet<String> tags, String profilePicture) {
        this.name = name;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.tags = new HashSet<String>();
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

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        if (tags.contains(tag)) {
            tags.remove(tag);
        }
    }

}
