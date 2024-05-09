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

public class ViewEventsActivity extends AppCompatActivity {

    private ListView listViewUpcomingEvents, listViewPastEvents;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter upcomingAdapter, pastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        listViewUpcomingEvents = findViewById(R.id.listViewUpcomingEvents);
        listViewPastEvents = findViewById(R.id.listViewPastEvents);
        Button btnReturnToMain = findViewById(R.id.btnReturnToMain);
        Button btnAddEvent = findViewById(R.id.btnAddEvent);
        dbHelper = new DatabaseHelper(this);

        setupAdapters();
        loadEvents();

        listViewUpcomingEvents.setOnItemClickListener(this::onItemClick);
        listViewPastEvents.setOnItemClickListener(this::onItemClick);

        btnAddEvent.setOnClickListener(v -> startActivity(new Intent(this, AddEventActivity.class)));
        btnReturnToMain.setOnClickListener(v -> finish());
    }

    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ViewEventsActivity.this, EditEventActivity.class);
        intent.putExtra("EVENT_ID", (int) id);
        startActivity(intent);
    }

    private void setupAdapters() {

        upcomingAdapter = createAdapter();
        pastAdapter = createAdapter();
        listViewUpcomingEvents.setAdapter(upcomingAdapter);
        listViewPastEvents.setAdapter(pastAdapter);
    }

    private SimpleCursorAdapter createAdapter() {
        String[] from = {DatabaseHelper.EVENT_NAME, DatabaseHelper.EVENT_LOCATION, DatabaseHelper.EVENT_DATE};
        int[] to = {R.id.textViewEventName, R.id.textViewEventLocation, R.id.textViewEventDate};
        return new SimpleCursorAdapter(this, R.layout.event_item, null, from, to, 0);
    }

    private void loadEvents() {
        Cursor upcomingEvents = dbHelper.getUpcomingEvents();
        Cursor pastEvents = dbHelper.getPastEvents();
        upcomingAdapter.changeCursor(upcomingEvents);
        pastAdapter.changeCursor(pastEvents);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }
}
