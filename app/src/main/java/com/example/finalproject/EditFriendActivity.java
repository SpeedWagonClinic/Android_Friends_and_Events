package com.example.finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to edit an existing friend's details.
 */

public class EditFriendActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, genderEditText, dobEditText, hobbiesEditText;
    private DatabaseHelper dbHelper;
    private int friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        dbHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        genderEditText = findViewById(R.id.genderEditText);
        dobEditText = findViewById(R.id.dobEditText);
        hobbiesEditText = findViewById(R.id.hobbiesEditText);
        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button cancelBtn = findViewById(R.id.cancelBtn1);

        friendId = getIntent().getIntExtra("FRIEND_ID", 0);
        loadFriendData();

        updateButton.setOnClickListener(v -> updateFriend());
        deleteButton.setOnClickListener(v -> deleteFriend());
        cancelBtn.setOnClickListener(v -> finish());
    }


    /**
     * Loads friend data from the database and populates the UI with the data.
     */

    @SuppressLint("Range")
    private void loadFriendData() {
        Cursor cursor = dbHelper.getFriend(friendId);
        if (cursor.moveToFirst()) {
            nameEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FRIEND_NAME)));
            phoneEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FRIEND_PHONE)));
            genderEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FRIEND_GENDER)));
            dobEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FRIEND_DOB)));
            hobbiesEditText.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FRIEND_HOBBIES)));
        }
        cursor.close();
    }

    /**
     * Updates the friend's details in the database with the data entered by the user.
     */
    private void updateFriend() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String hobbies = hobbiesEditText.getText().toString();

        dbHelper.updateFriend(friendId, name, phone, gender, dob, hobbies);
        finish();
    }

    /**
     * Deletes the friend from the database.
     */
    private void deleteFriend() {
        dbHelper.deleteFriend(friendId);
        finish();
    }
}

