package com.example.finalproject;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Main activity class that serves as the entry point for the application.
 * This activity provides navigation buttons to view friends and events.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the button to navigate to the ViewFriendsActivity
        Button btnViewFriends = findViewById(R.id.btnViewFriends);
        btnViewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToViewFriendsActivity();
            }
        });

        // Setup the button to navigate to the ViewEventsActivity
        Button btnViewEvents = findViewById(R.id.btnViewEvents);
        btnViewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToViewEventsActivity();
            }
        });
    }

    /**
     * Starts the ViewFriendsActivity to display a list of friends.
     */
    private void navigateToViewFriendsActivity() {
        Intent intent = new Intent(MainActivity.this, ViewFriendsActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the ViewEventsActivity to display a list of events.
     */
    private void navigateToViewEventsActivity() {
        Intent intent = new Intent(MainActivity.this, ViewEventsActivity.class);
        startActivity(intent);
    }
}
