package com.example.lostfoundapp;

public class LostFoundItem {

    int id;
    String type;
    String name;
    String category;
    String description;
    String location;
    String image;
    String dateTime;

    public LostFoundItem(int id, String type, String name, String category, String description, String location, String image, String dateTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.image = image;
        this.dateTime = dateTime;
    }
}
