package com.example.studybuddy.model;

import java.util.HashSet;

public class Pin {
    private String course;
    private int count;
    private int capacity;
    private HashSet<User> listOfUsers;
    private String location;
    private String groupName;

    public Pin(String course, int capacity, String location, User admin, String groupName) {
        this.course = course;
        this.count = 0;
        this.capacity = capacity;
        this.listOfUsers = new HashSet<User>();
        this.location = location;
        this.groupName = groupName;

        addUser(admin);
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCount(int count) {
        this.capacity = count;
    }

    public int getCount() {
        return count;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getcapacity() {
        return capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void addUser(User user) {
        if (!listOfUsers.contains(user) && count < capacity) {
            listOfUsers.add(user);
            count++;
        }
    }

    public void removeUser(User user) {
        if (listOfUsers.contains(user)) {
            listOfUsers.remove(user);
            count--;
        }
    }

    public Boolean filterByCourse(String filterName) {
        if (this.course.equals(filterName)) {
            return true;
        }
        return false;
    }
}
