package com.example.finalproject;


public class Friend {
    private final int id;
    private final String name;
    private final String phone;
    private final String gender;
    private boolean isSelected;

    public Friend(int id, String name, String phone, String gender) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.isSelected = false;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
