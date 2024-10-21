package com.example.muskankatiyar;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "replies.db";
    private static final String TABLE_NAME = "replies";
    private static final String COL_ID = "id";
    private static final String COL_TIME_TAKEN = "time_taken"; // Store as a decimal or integer
    private static final String COL_INCENTIVE = "incentive";   // Store as an integer or decimal
    private static final String COL_TECH_STACK = "tech_stack";
    // Define a new column for the contact number
    private static final String COL_CONTACT_NUMBER = "contact_number";
    private Context context;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CONTACT_NUMBER + " TEXT, "  // Add contact number here
                + COL_TIME_TAKEN + " REAL, "
                + COL_INCENTIVE + " REAL, "
                + COL_TECH_STACK + " TEXT ) "; // Add contact number as a text field;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addReply(String timeTaken, String incentive, String techStack,String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Convert time taken to a numeric value (for example, from "2 hours" to 2.0)
        float numericTimeTaken = convertTimeToDecimal(timeTaken); // Implement this method as needed
        float numericIncentive = Float.parseFloat(incentive.replace("$", "").replace(",", "").trim());

        // Insert the contact number and reply details
        contentValues.put(COL_CONTACT_NUMBER, contactNumber); // Store contact number
        contentValues.put(COL_TIME_TAKEN, numericTimeTaken);
        contentValues.put(COL_INCENTIVE, numericIncentive);
        contentValues.put(COL_TECH_STACK, techStack);


        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    // Example conversion method
    private float convertTimeToDecimal(String timeTaken) {
        // Assuming time format is "X hours Y minutes"
        String[] parts = timeTaken.split(" ");
        float totalHours = 0;

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].toLowerCase().contains("hour")) {
                totalHours += Float.parseFloat(parts[i - 1]);
            } else if (parts[i].toLowerCase().contains("minute")) {
                totalHours += Float.parseFloat(parts[i - 1]) / 60;
            }
        }
        return totalHours;
    }

    public ArrayList<String> getAllReplies() {
        ArrayList<String> replies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to select all replies sorted by time taken and incentive
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIME_TAKEN + " ASC, " + COL_INCENTIVE + " ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    // Extract all columns, including the contact number
                    String contactNumber = cursor.getString(cursor.getColumnIndex(COL_CONTACT_NUMBER));
                    String timeTaken = cursor.getString(cursor.getColumnIndex(COL_TIME_TAKEN));
                    String incentive = cursor.getString(cursor.getColumnIndex(COL_INCENTIVE));
                    String techStack = cursor.getString(cursor.getColumnIndex(COL_TECH_STACK));

                    // Format the string to include the contact number
                    String reply = "Contact: " + contactNumber +
                            ", Time Taken: " + timeTaken +
                            ", Incentive: " + incentive +
                            ", Tech Stack: " + techStack;

                    // Add to the list of replies
                    replies.add(reply);

                    // Log the data for debugging
                    Log.d("Database", reply);

                } while (cursor.moveToNext());
            } else {
                // If no data is found, show a Toast
                Log.d("Database", "No replies found in the database");


                Toast.makeText(this.context, "No replies in the database", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle errors and show a Toast with the error message
            Log.e("DatabaseError", e.getMessage());
            Toast.makeText(this.context, "Error retrieving replies: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();  // Ensure the cursor is closed to avoid memory leaks
            }
            db.close();  // Close the database connection
        }

        return replies;
    }




    public String[] getBestContactAndTechStack() {
        String[] result = new String[2];  // Array to hold the best contact number and tech stack
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to get the contact with the least time and incentive
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIME_TAKEN + " ASC, " + COL_INCENTIVE + " ASC LIMIT 1", null);

            if (cursor.moveToFirst()) {
                // Extract contact number and tech stack of the best contact
                String contactNumber = cursor.getString(cursor.getColumnIndex(COL_CONTACT_NUMBER));
                String techStack = cursor.getString(cursor.getColumnIndex(COL_TECH_STACK));

                result[0] = contactNumber;  // Store the contact number
                result[1] = techStack;      // Store the tech stack
            } else {
                // If no data found, return empty strings
                result[0] = "";
                result[1] = "";
                Toast.makeText(this.context, "No replies found in the database", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseError", e.getMessage());
            Toast.makeText(this.context, "Error retrieving best contact: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return result;  // Return the array with contact number and tech stack
    }















}

