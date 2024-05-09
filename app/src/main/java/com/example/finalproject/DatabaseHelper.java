package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final int DATABASE_VERSION = 2;

    // Tables
    private static final String TABLE_FRIENDS = "friends";
    private static final String TABLE_EVENTS = "events";

    // Friends Table
    public static final String FRIEND_ID = "_id";
    public static final String FRIEND_NAME = "name";
    public static final String FRIEND_PHONE = "phone";
    public static final String FRIEND_GENDER = "gender";

    // Events Table
    public static final String EVENT_ID = "_id";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_LOCATION = "location";
    public static final String EVENT_DATE = "date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FRIENDS + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + FRIEND_NAME + " TEXT, " + FRIEND_PHONE + " TEXT, " + FRIEND_GENDER + " TEXT, " + "dob TEXT, " + "hobbies TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_NAME + " TEXT, " + EVENT_LOCATION + " TEXT, " + EVENT_DATE + " TEXT)");


        db.execSQL("CREATE TABLE IF NOT EXISTS event_friends (" + "event_id INTEGER, " + "friend_id INTEGER, " + "FOREIGN KEY (event_id) REFERENCES " + TABLE_EVENTS + "(_id), " + "FOREIGN KEY (friend_id) REFERENCES " + TABLE_FRIENDS + "(_id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS event_friends (" + "event_id INTEGER, " + "friend_id INTEGER, " + "FOREIGN KEY (event_id) REFERENCES " + TABLE_EVENTS + "(_id), " + "FOREIGN KEY (friend_id) REFERENCES " + TABLE_FRIENDS + "(_id))");
        }

    }


    public long addFriend(String name, String phone, String gender, String dob, String hobbies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FRIEND_NAME, name);
        values.put(FRIEND_PHONE, phone);
        values.put(FRIEND_GENDER, gender);
        values.put("dob", dob);
        values.put("hobbies", hobbies);
        long id = db.insert(TABLE_FRIENDS, null, values);
        db.close();
        return id;
    }


    public Cursor getAllFriends() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FRIENDS, null);
    }

    public List<Friend> getAllFriendsAsList() {
        List<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FRIENDS, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(FRIEND_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(FRIEND_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(FRIEND_PHONE));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(FRIEND_GENDER));
                friends.add(new Friend(id, name, phone, gender));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return friends;
    }

    public void addFriendsToEvent(int eventId, List<Integer> friendIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int friendId : friendIds) {
                values.clear();
                values.put("event_id", eventId);
                values.put("friend_id", friendId);
                db.insert("event_friends", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    @SuppressLint("Range")
    public List<Friend> getFriendsForEvent(int eventId) {
        List<Friend> friends = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT f._id, f.name, f.phone, f.gender FROM friends f INNER JOIN event_friends ef ON f._id = ef.friend_id WHERE ef.event_id = ?", new String[]{String.valueOf(eventId)});
        while (cursor.moveToNext()) {
            friends.add(new Friend(cursor.getInt(cursor.getColumnIndex(FRIEND_ID)), cursor.getString(cursor.getColumnIndex(FRIEND_NAME)), cursor.getString(cursor.getColumnIndex(FRIEND_PHONE)), cursor.getString(cursor.getColumnIndex(FRIEND_GENDER))));
        }
        cursor.close();
        db.close();
        return friends;
    }


    public void updateFriend(int id, String name, String phone, String gender, String dob, String hobbies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FRIEND_NAME, name);
        values.put(FRIEND_PHONE, phone);
        values.put(FRIEND_GENDER, gender);
        values.put(COLUMN_FRIEND_DOB, dob);
        values.put(COLUMN_FRIEND_HOBBIES, hobbies);
        db.update(TABLE_FRIENDS, values, FRIEND_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public void deleteFriend(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, FRIEND_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public long addEvent(String name, String location, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, name);
        values.put(EVENT_LOCATION, location);
        values.put(EVENT_DATE, date);
        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }


    public int updateEvent(int id, String name, String location, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, name);
        values.put(EVENT_LOCATION, location);
        values.put(EVENT_DATE, date);
        return db.update(TABLE_EVENTS, values, EVENT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void updateEventFriends(int eventId, List<Integer> friendIds) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("event_friends", "event_id = ?", new String[]{String.valueOf(eventId)});


        for (int friendId : friendIds) {
            ContentValues values = new ContentValues();
            values.put("event_id", eventId);
            values.put("friend_id", friendId);
            db.insert("event_friends", null, values);
        }
        db.close();
    }

    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, EVENT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getEvent(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EVENTS, new String[]{"_id", EVENT_NAME, EVENT_LOCATION, EVENT_DATE}, "_id = ?", new String[]{String.valueOf(eventId)}, null, null, null);
    }


    public static final String COLUMN_FRIEND_DOB = "dob";
    public static final String COLUMN_FRIEND_HOBBIES = "hobbies";
    public static final String COLUMN_FRIEND_NAME = "name";
    public static final String COLUMN_FRIEND_PHONE = "phone";
    public static final String COLUMN_FRIEND_GENDER = "gender";


    public Cursor getFriend(int friendId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"_id", FRIEND_NAME, FRIEND_PHONE, FRIEND_GENDER, "dob", "hobbies"};
        return db.query(TABLE_FRIENDS, projection, "_id = ?", new String[]{String.valueOf(friendId)}, null, null, null);

    }

    public Cursor getUpcomingEvents() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return getReadableDatabase().query(TABLE_EVENTS, new String[]{"_id", EVENT_NAME, EVENT_LOCATION, EVENT_DATE}, EVENT_DATE + " >= ?", new String[]{currentDate}, null, null, EVENT_DATE + " ASC");
    }

    public Cursor getPastEvents() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return getReadableDatabase().query(TABLE_EVENTS, new String[]{"_id", EVENT_NAME, EVENT_LOCATION, EVENT_DATE}, EVENT_DATE + " < ?", new String[]{currentDate}, null, null, EVENT_DATE + " DESC");
    }

    public Cursor searchFriends(String searchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FRIENDS, new String[]{"_id", FRIEND_NAME, FRIEND_PHONE, FRIEND_GENDER}, FRIEND_NAME + " LIKE ? OR " + FRIEND_PHONE + " LIKE ? OR hobbies LIKE ?", new String[]{"%" + searchQuery + "%", "%" + searchQuery + "%", "%" + searchQuery + "%"}, null, null, null);
    }


}



