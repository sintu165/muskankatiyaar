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
    private static final String COL_TIME_TAKEN = "time_taken";
    private static final String COL_INCENTIVE = "incentive";
    private static final String COL_TECH_STACK = "tech_stack";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME_TAKEN + " TEXT, " +
                COL_INCENTIVE + " TEXT, " +
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
        contentValues.put(COL_TIME_TAKEN, timeTaken);
        contentValues.put(COL_INCENTIVE, incentive);
        contentValues.put(COL_TECH_STACK, techStack);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public ArrayList<String> getAllReplies() {
        ArrayList<String> replies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
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
    public void addDummyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // Add first dummy reply
        contentValues.put(COL_TIME_TAKEN, "2 hours");
        contentValues.put(COL_INCENTIVE, "$100");
        contentValues.put(COL_TECH_STACK, "Java");
        db.insert(TABLE_NAME, null, contentValues);

        // Add second dummy reply
        contentValues.clear(); // Clear previous values
        contentValues.put(COL_TIME_TAKEN, "3 hours");
        contentValues.put(COL_INCENTIVE, "$150");
        contentValues.put(COL_TECH_STACK, "Kotlin");
        db.insert(TABLE_NAME, null, contentValues);

        // Add third dummy reply
        contentValues.clear(); // Clear previous values
        contentValues.put(COL_TIME_TAKEN, "1.5 hours");
        contentValues.put(COL_INCENTIVE, "$80");
        contentValues.put(COL_TECH_STACK, "Python");
        db.insert(TABLE_NAME, null, contentValues);

        db.close();
    }


}

