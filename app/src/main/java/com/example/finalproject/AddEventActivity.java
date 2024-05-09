package com.example.finalproject;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextEventLocation, editTextEventDate;
    private DatabaseHelper dbHelper;
    private FriendsAdapter friendsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        dbHelper = new DatabaseHelper(this);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        RecyclerView recyclerViewSelectFriends = findViewById(R.id.recyclerViewSelectFriends);
        Button buttonAddEvent = findViewById(R.id.buttonAddEvent);
        Button cancelBtn = findViewById(R.id.cancelBtn);


        recyclerViewSelectFriends.setLayoutManager(new LinearLayoutManager(this));
        friendsAdapter = new FriendsAdapter(new ArrayList<>(), new ArrayList<>());
        recyclerViewSelectFriends.setAdapter(friendsAdapter);


        loadFriends();

        buttonAddEvent.setOnClickListener(view -> addEvent());
        cancelBtn.setOnClickListener(v -> finish());
    }


    private void loadFriends() {
        Cursor cursor = dbHelper.getAllFriends();
        List<Friend> friends = new ArrayList<>();
        List<Integer> selectedFriendIds = new ArrayList<>();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FRIEND_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FRIEND_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FRIEND_PHONE));
            @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FRIEND_GENDER));
            friends.add(new Friend(id, name, phone, gender));
        }
        cursor.close();

        friendsAdapter.updateFriendsList(friends, selectedFriendIds);
    }


    private void addEvent() {
        String name = editTextEventName.getText().toString().trim();
        String location = editTextEventLocation.getText().toString().trim();
        String date = editTextEventDate.getText().toString().trim();

        if (!name.isEmpty() && !location.isEmpty() && !date.isEmpty()) {
            long eventId = dbHelper.addEvent(name, location, date);
            if (eventId > 0) {
                List<Integer> selectedFriendIds = friendsAdapter.getSelectedFriendsIds();
                dbHelper.addFriendsToEvent((int) eventId, selectedFriendIds);
                Toast.makeText(this, "Event added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add event.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        }
    }


}
