package com.example.muskankatiyar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;

public class RepliesActivity extends AppCompatActivity {

    private ListView repliesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        repliesListView = findViewById(R.id.replies_list_view);

        try {
            DatabaseHelper db = new DatabaseHelper(this);

          //get data from db stor into array of sting
            ArrayList<String> replies = db.getAllReplies();





            Toast.makeText(this, "Retrieved " + replies.size() + " replies", Toast.LENGTH_SHORT).show();

            if (replies.isEmpty()) {
                Toast.makeText(this, "No replies found.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Loading replies...", Toast.LENGTH_SHORT).show();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, replies);
            repliesListView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}

