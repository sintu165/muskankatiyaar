package com.example.muskankatiyar;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "replies.db";
    private static final String TABLE_NAME = "replies";
    private static final String COL_ID = "id";
    private static final String COL_TIME_TAKEN = "time_taken"; // Store as a decimal or integer
    private static final String COL_INCENTIVE = "incentive";

    private static final String COL_TECH_STACK = "tech_stack";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 COL_TIME_TAKEN + " REAL, " +
                 COL_INCENTIVE + " REAL, "+
                COL_TECH_STACK + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addReply(String timeTaken, String incentive, String techStack) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Convert time taken to a numeric value (for example, from "2 hours" to 2.0)
        float numericTimeTaken = convertTimeToDecimal(timeTaken); // Implement this method as needed
        float numericIncentive = Float.parseFloat(incentive.replace("$", "").replace(",", "").trim());

        contentValues.put(COL_TIME_TAKEN, timeTaken);
        contentValues.put(COL_INCENTIVE, incentive);
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_TIME_TAKEN + " ASC, " + COL_INCENTIVE + " ASC", null);
        if (cursor.moveToFirst()) {
            do {
                String reply = "Time Taken: " + cursor.getString(1) +
                        ", Incentive: " + cursor.getString(2) +
                        ", Tech Stack: " + cursor.getString(3);
                replies.add(reply);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return replies;
    }



}

