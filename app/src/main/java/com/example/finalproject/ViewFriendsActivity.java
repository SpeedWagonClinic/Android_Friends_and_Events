package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Button;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for viewing and searching friends.
 */
public class ViewFriendsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);

        editTextSearch = findViewById(R.id.editTextSearch);
        ListView listViewFriends = findViewById(R.id.listViewFriends);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button btnReturnToMain = findViewById(R.id.btnReturnToMain);
        Button btnAddFriend = findViewById(R.id.btnAddFriend);

        dbHelper = new DatabaseHelper(this);
        initializeListView(listViewFriends);
        setupButtons(buttonSearch, btnReturnToMain, btnAddFriend);

        loadFriends();
    }

    /**
     * Initializes the list view with a cursor adapter and item click listener.
     */
    private void initializeListView(ListView listView) {
        String[] columns = {DatabaseHelper.FRIEND_NAME, DatabaseHelper.FRIEND_PHONE, DatabaseHelper.FRIEND_GENDER};
        int[] toViews = {R.id.textViewFriendName, R.id.textViewFriendPhone, R.id.textViewFriendGender};
        adapter = new SimpleCursorAdapter(this, R.layout.friend_item, null, columns, toViews, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ViewFriendsActivity.this, EditFriendActivity.class);
            intent.putExtra("FRIEND_ID", (int) id);
            startActivity(intent);
        });
    }

    /**
     * Configures the search, return, and add friend buttons with click listeners.
     */
    private void setupButtons(Button searchButton, Button returnButton, Button addButton) {
        searchButton.setOnClickListener(v -> performSearch());
        returnButton.setOnClickListener(v -> finish());
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewFriendsActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Performs a search based on the query provided in the editTextSearch.
     * Reloads all friends if the search query is empty.
     */
    private void performSearch() {
        String searchQuery = editTextSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchQuery)) {
            searchFriends(searchQuery);
        } else {
            loadFriends();
        }
    }

    /**
     * Searches friends in the database using the provided query and updates the list view.
     */
    private void searchFriends(String query) {
        Cursor cursor = dbHelper.searchFriends(query);
        adapter.changeCursor(cursor);
    }

    /**
     * Loads all friends from the database and updates the list view.
     */
    private void loadFriends() {
        Cursor cursor = dbHelper.getAllFriends();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFriends();  // Ensure the friends list is refreshed whenever the activity resumes.
    }
}
