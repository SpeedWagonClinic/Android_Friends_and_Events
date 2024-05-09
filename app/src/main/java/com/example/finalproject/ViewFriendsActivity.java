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

public class ViewFriendsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private EditText editTextSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);
        editTextSearch = findViewById(R.id.editTextSearch);
        Button buttonSearch = findViewById(R.id.buttonSearch);


        ListView listViewFriends = findViewById(R.id.listViewFriends);
        Button btnReturnToMain = findViewById(R.id.btnReturnToMain);
        Button btnAddFriend = findViewById(R.id.btnAddFriend);
        dbHelper = new DatabaseHelper(this);
        buttonSearch.setOnClickListener(v -> performSearch());


        String[] columns = new String[]{

                DatabaseHelper.FRIEND_NAME, DatabaseHelper.FRIEND_PHONE,DatabaseHelper.FRIEND_GENDER};
        int[] toViews = new int[]{R.id.textViewFriendName, R.id.textViewFriendPhone, R.id.textViewFriendGender};
        adapter = new SimpleCursorAdapter(this, R.layout.friend_item, null, columns, toViews, 0);
        listViewFriends.setAdapter(adapter);


        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Intent intent = new Intent(ViewFriendsActivity.this, EditFriendActivity.class);
                intent.putExtra("FRIEND_ID", (int) id);
                startActivity(intent);
            }
        });

        btnReturnToMain.setOnClickListener(v -> {
            finish();
        });
        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(ViewFriendsActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });

        loadFriends();
    }

    private void performSearch() {
        String searchQuery = editTextSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchQuery)) {
            searchFriends(searchQuery);
        } else {
            loadFriends();
        }
    }

    private void searchFriends(String query) {
        Cursor cursor = dbHelper.searchFriends(query);
        adapter.changeCursor(cursor);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFriends();
    }

    private void loadFriends() {
        Cursor cursor = dbHelper.getAllFriends();
        adapter.changeCursor(cursor);
    }
}
