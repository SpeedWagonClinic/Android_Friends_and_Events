package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for viewing lists of upcoming and past events.
 */
public class ViewEventsActivity extends AppCompatActivity {

    private ListView listViewUpcomingEvents, listViewPastEvents;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter upcomingAdapter, pastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        initializeViews();
        dbHelper = new DatabaseHelper(this);

        setupAdapters();
        loadEvents();

        setListeners();
    }

    /**
     * Initializes all views in this activity.
     */
    private void initializeViews() {
        listViewUpcomingEvents = findViewById(R.id.listViewUpcomingEvents);
        listViewPastEvents = findViewById(R.id.listViewPastEvents);
        Button btnReturnToMain = findViewById(R.id.btnReturnToMain);
        Button btnAddEvent = findViewById(R.id.btnAddEvent);

        btnAddEvent.setOnClickListener(v -> startActivity(new Intent(this, AddEventActivity.class)));
        btnReturnToMain.setOnClickListener(v -> finish());
    }

    /**
     * Sets up listeners for list item clicks, which navigate to the event editing screen.
     */
    private void setListeners() {
        listViewUpcomingEvents.setOnItemClickListener(this::onItemClick);
        listViewPastEvents.setOnItemClickListener(this::onItemClick);
    }

    /**
     * Handles the click of a list item by opening the EditEventActivity.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked.
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ViewEventsActivity.this, EditEventActivity.class);
        intent.putExtra("EVENT_ID", (int) id);
        startActivity(intent);
    }

    /**
     * Sets up the adapters for the upcoming and past event list views.
     */
    private void setupAdapters() {
        upcomingAdapter = createAdapter();
        pastAdapter = createAdapter();
        listViewUpcomingEvents.setAdapter(upcomingAdapter);
        listViewPastEvents.setAdapter(pastAdapter);
    }

    /**
     * Creates a SimpleCursorAdapter for event list views.
     *
     * @return a configured SimpleCursorAdapter.
     */
    private SimpleCursorAdapter createAdapter() {
        String[] from = {DatabaseHelper.EVENT_NAME, DatabaseHelper.EVENT_LOCATION, DatabaseHelper.EVENT_DATE};
        int[] to = {R.id.textViewEventName, R.id.textViewEventLocation, R.id.textViewEventDate};
        return new SimpleCursorAdapter(this, R.layout.event_item, null, from, to, 0);
    }

    /**
     * Loads upcoming and past events from the database and updates the list views.
     */
    private void loadEvents() {
        Cursor upcomingEvents = dbHelper.getUpcomingEvents();
        Cursor pastEvents = dbHelper.getPastEvents();
        upcomingAdapter.changeCursor(upcomingEvents);
        pastAdapter.changeCursor(pastEvents);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();  // Refresh events every time the activity resumes.
    }
}
