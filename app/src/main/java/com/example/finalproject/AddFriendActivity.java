package com.example.finalproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddFriendActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextGender, editTextDOB, editTextHobbies;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        dbHelper = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextGender = findViewById(R.id.editTextGender);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextHobbies = findViewById(R.id.editTextHobbies);
        Button buttonAddFriend = findViewById(R.id.buttonAddFriend);
        Button cancelBtn = findViewById(R.id.cancelBtn);

        buttonAddFriend.setOnClickListener(view -> {
            addFriend();
        });
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void addFriend() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String dob = editTextDOB.getText().toString().trim();
        String hobbies = editTextHobbies.getText().toString().trim();

        if (!name.isEmpty() && !phone.isEmpty() && !gender.isEmpty() && !dob.isEmpty() && !hobbies.isEmpty()) {
            long id = dbHelper.addFriend(name, phone, gender, dob, hobbies);
            if (id > 0) {
                Toast.makeText(this, "Friend added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add friend.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
