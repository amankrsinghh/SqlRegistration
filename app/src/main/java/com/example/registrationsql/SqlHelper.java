package com.example.registrationsql;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserRegistration.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";

    private Context context;



    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_ADDRESS + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertUser(String name, String email, String password, String address, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Toast.makeText(context, "Failed to Register Or User Already Exists!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, login.class);
            context.startActivity(intent);
            ((MainActivity) context).finish();
        }
    }

    // Method to verify login credentials
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean isValid = cursor.getCount() > 0; // Check if any matching record exists
        cursor.close();
        return isValid;
    }

    // Method to fetch user data by email
    public String fetchUserData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT name, address, phone FROM users WHERE email = ?";
        String[] selectionArgs = {email};


        Cursor cursor = db.rawQuery(query, selectionArgs);

        String  userName = null;
        String userAddress= null;
        String userPhone= null;
        if (cursor.moveToFirst()) {
             userName = cursor.getString(cursor.getColumnIndexOrThrow("name")); // Fetch the "name"
            userAddress = cursor.getString(cursor.getColumnIndexOrThrow("address")); // Fetch the "address"
            userPhone = cursor.getString(cursor.getColumnIndexOrThrow("phone")); // Fetch the "phone"

        }
        cursor.close();
        return userName +"✅"+ "\n"+ "Address: " + userAddress +"\n"+ "Phone: " + userPhone;

    }

}
