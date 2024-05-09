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

public class EditEventActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextEventLocation, editTextEventDate;
    private DatabaseHelper dbHelper;
    private int eventId;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        dbHelper = new DatabaseHelper(this);
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventLocation = findViewById(R.id.editTextEventLocation);
        editTextEventDate = findViewById(R.id.editTextEventDate);
        RecyclerView recyclerViewAttendingFriends = findViewById(R.id.recyclerViewAttendingFriends);
        Button buttonUpdateEvent = findViewById(R.id.buttonUpdateEvent);
        Button buttonDeleteEvent = findViewById(R.id.buttonDeleteEvent);
        Button cancelBtn1 = findViewById(R.id.cancelBtn1);

        eventId = getIntent().getIntExtra("EVENT_ID", 0);


        friendsAdapter = new FriendsAdapter(new ArrayList<>(), new ArrayList<>());
        recyclerViewAttendingFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAttendingFriends.setAdapter(friendsAdapter);

        loadEventData();

        buttonUpdateEvent.setOnClickListener(v -> updateEvent());
        buttonDeleteEvent.setOnClickListener(v -> deleteEvent());
        cancelBtn1.setOnClickListener(v -> finish());
    }

    private void loadEventData() {
        Cursor cursor = dbHelper.getEvent(eventId);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_NAME));
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_LOCATION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_DATE));

            editTextEventName.setText(name);
            editTextEventLocation.setText(location);
            editTextEventDate.setText(date);

            List<Friend> allFriends = dbHelper.getAllFriendsAsList();
            List<Friend> attendingFriends = dbHelper.getFriendsForEvent(eventId);


            List<Integer> attendingFriendIds = new ArrayList<>();
            for (Friend friend : attendingFriends) {
                attendingFriendIds.add(friend.getId());
                for (Friend allFriend : allFriends) {
                    if (allFriend.getId() == friend.getId()) {
                        allFriend.setSelected(true);
                        break;
                    }
                }
            }

            friendsAdapter.updateFriendsList(allFriends, attendingFriendIds);

            cursor.close();
        } else {
            Toast.makeText(this, "Error loading event.", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateEvent() {
        String name = editTextEventName.getText().toString().trim();
        String location = editTextEventLocation.getText().toString().trim();
        String date = editTextEventDate.getText().toString().trim();

        if (!name.isEmpty() && !location.isEmpty() && !date.isEmpty()) {
            int result = dbHelper.updateEvent(eventId, name, location, date);
            if (result > 0) {
                List<Integer> selectedFriendIds = friendsAdapter.getSelectedFriendsIds();
                dbHelper.updateEventFriends(eventId, selectedFriendIds);
                Toast.makeText(this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update event.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteEvent() {
        dbHelper.deleteEvent(eventId);
        Toast.makeText(this, "Event deleted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
