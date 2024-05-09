package com.example.finalproject;

/**
 * Represents a friend with details like name, phone number, and gender.
 * This class also includes functionality to mark a friend as selected,
 * which can be useful in contexts where friends need to be chosen or marked,
 * such as listing friends for an event.
 */
public class Friend {
    private final int id;
    private final String name;
    private final String phone;
    private final String gender;
    private boolean isSelected;

    /**
     * Constructs a new Friend instance.
     *
     * @param id The unique identifier for the friend.
     * @param name The friend's name.
     * @param phone The friend's phone number.
     * @param gender The friend's gender.
     */
    public Friend(int id, String name, String phone, String gender) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.isSelected = false; // Default to not selected.
    }

    // Getters

    /**
     * Returns the unique identifier for the friend.
     * @return the friend's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the friend.
     * @return the friend's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the phone number of the friend.
     * @return the friend's phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the gender of the friend.
     * @return the friend's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Returns whether the friend is currently selected.
     * @return true if selected, false otherwise.
     */
    public boolean isSelected() {
        return isSelected;
    }

    // Setters

    /**
     * Sets the friend's selection status.
     * @param selected true to mark as selected, false otherwise.
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Provides a string representation of the friend, useful for debugging.
     * @return a string description of the friend.
     */
    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
