package com.example.muskankatiyar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SEND_SMS = 123;

    private LinearLayout contactsContainer;
    private Button addContactButton, submitButton;
    private ArrayList<EditText> contactFields;
    private EditText requestField, deadlineField, incentiveField;

    private static final int PERMISSION_REQUEST_RECEIVE_SMS = 123;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_RECEIVE_SMS);
        }


        contactsContainer = findViewById(R.id.contacts_container);
        addContactButton = findViewById(R.id.add_contact_button);
        submitButton = findViewById(R.id.submit_button);
        contactFields = new ArrayList<>();




        // Fields for request, deadline, and incentive
        requestField = findViewById(R.id.request_field);
        deadlineField = findViewById(R.id.deadline_field);
        incentiveField = findViewById(R.id.incentive_field);

       Button viewRepliesButton = findViewById(R.id.view_replies_button);


        // Create DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);






        // Add the first contact field initially
        addContactField();

        // Add a new contact field when the button is clicked
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactField();
            }
        });

        // Handle form submission and broadcast message
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
                } else {
                    broadcastMessage();
                    // Start the RepliesActivity after sending messages
                    Intent intent = new Intent(MainActivity.this, RepliesActivity.class);
                    startActivity(intent);
                }
            }
        });




        // Handle viewing replies
        viewRepliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RepliesActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Navigating to replies...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Method to dynamically add a new contact number input field
    private void addContactField() {
        EditText contactField = new EditText(this);
        contactField.setHint("Enter contact number");
        contactField.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        contactsContainer.addView(contactField);
        contactFields.add(contactField);
    }

    // Method to send SMS to all entered contacts
    private void broadcastMessage() {
        ArrayList<String> contactNumbers = new ArrayList<>();

        // Collect all contact numbers from the input fields
        for (EditText field : contactFields) {
            String contactNumber = field.getText().toString().trim();
            if (!contactNumber.isEmpty()) {
                contactNumbers.add(contactNumber);
            }
        }

        // Get the request, deadline, and incentive values
        String request = requestField.getText().toString().trim();
        String deadline = deadlineField.getText().toString().trim();
        String incentive = incentiveField.getText().toString().trim();

        // Check if at least one contact number and the fields are filled
        if (contactNumbers.isEmpty() || request.isEmpty() || deadline.isEmpty() || incentive.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and enter at least one contact number", Toast.LENGTH_SHORT).show();
        } else {
            // Prepare the message
            String message = "Request: " + request + "\nDeadline: " + deadline + "\nIncentive: " + incentive;

            // Send SMS to each contact number
            SmsManager smsManager = SmsManager.getDefault();
            for (String contact : contactNumbers) {
                smsManager.sendTextMessage(contact, null, message, null, null);
            }

            Toast.makeText(this, "Message sent to all contacts!", Toast.LENGTH_LONG).show();
        }
    }

    // Handle the result of the SMS permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                broadcastMessage();
            } else {
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}









