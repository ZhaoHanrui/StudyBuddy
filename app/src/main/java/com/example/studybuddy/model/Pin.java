package com.example.studybuddy.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;

public class Pin {
    private String course;
    private Availability capacity;
    private LatLng location;
    private String locationSpecifics;
    private User creator;
    private HashSet<User> listOfUsers;


    private enum Availability {
        VACANT, FILLED, FULL;
    }

    public Pin(String courseSubject, int number, LatLng location, String locationSpecifics, User creator) {
        this.course = courseSubject + "" + number;
        this.capacity = Availability.VACANT;
        this.location = location;
        this.locationSpecifics = locationSpecifics;
        this.creator = creator;
        this.listOfUsers = new HashSet<>();
    }

    public String getCourse() {
        return course;
    }

    public Availability getCapacity() {
        return capacity;
    }

    public void setCapacity(Availability capacity) {
        this.capacity = capacity;
    }

    public LatLng getLocation() {
        return location;
    }
}
