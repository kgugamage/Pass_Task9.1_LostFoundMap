package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lost_found.db";
    public static final String TABLE_NAME = "items";

    public static final String COL_ID = "id";
    public static final String COL_TYPE = "type";
    public static final String COL_NAME = "name";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LOCATION = "location";
    public static final String COL_IMAGE = "image";
    public static final String COL_DATE = "date_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TYPE + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_IMAGE + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem(String type, String name, String category, String description, String location, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        ContentValues values = new ContentValues();
        values.put(COL_TYPE, type);
        values.put(COL_NAME, name);
        values.put(COL_CATEGORY, category);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_LOCATION, location);
        values.put(COL_IMAGE, image);
        values.put(COL_DATE, currentDateTime);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC", null);
    }

    public Cursor getItemsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (category.equals("All")) {
            return getAllItems();
        }

        return db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_CATEGORY + "=? ORDER BY " + COL_ID + " DESC",
                new String[]{category}
        );
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
