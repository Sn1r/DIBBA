package com.example.dibba;

import android.content.SharedPreferences;
import android.database.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;

public class UserProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.dibba.UserProvider";
    static final String URL = "content://com.example.dibba.UserProvider/users";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String id = "id";
    static final String name = "name";
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static HashMap<String, String> values;

    static final String TABLE_NAME = "Employees";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "users", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "users/*", uriCode);
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean recordsAdded = preferences.getBoolean("initial_records_added", false);

        if (db != null && !recordsAdded) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(name, "John");
            db.insert(TABLE_NAME, null, initialValues);

            initialValues.clear();
            initialValues.put(name, "Jane");
            db.insert(TABLE_NAME, null, initialValues);

            initialValues.clear();
            initialValues.put(name, "Jimmy");
            db.insert(TABLE_NAME, null, initialValues);

            initialValues.clear();
            initialValues.put(name, "DIBBA{1n53Cur3-C0n73N7-PR0v1D3r}");
            db.insert(TABLE_NAME, null, initialValues);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("initial_records_added", true);
            editor.apply();

            return true;
        }
        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        Cursor c = db.query(TABLE_NAME, projection, selection, selectionArgs, null,
                null, sortOrder);

        StringBuilder result = new StringBuilder();
        while (c.moveToNext()) {
            int idIndex = c.getColumnIndex(id);
            int nameIndex = c.getColumnIndex(name);
            int recordId = c.getInt(idIndex);
            String recordName = c.getString(nameIndex);
            result.append("ID: ").append(recordId).append(", Name: ").append(recordName).append("\n");
        }

        Log.d("UserProvider", "Query Result:\n" + result.toString());

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            db = dbHelper.getWritableDatabase();

            String nameValue = values.getAsString(name);
            if (nameValue != null) {
                if (isSQLInjectionAttempt(nameValue)) {
                    Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        StringBuilder result = new StringBuilder();
                        while (!cursor.isAfterLast()) {
                            int idIndex = cursor.getColumnIndex(id);
                            int nameIndex = cursor.getColumnIndex(name);
                            int recordId = cursor.getInt(idIndex);
                            String recordName = cursor.getString(nameIndex);
                            result.append("Record ID: ").append(recordId).append(", Name: ").append(recordName).append("\n");
                            cursor.moveToNext();
                        }
                        cursor.close();
                        Log.d("UserProvider", "Existing Records:\n" + result.toString());
                        return uri;
                    }
                }

                String sqlQuery = "INSERT INTO " + TABLE_NAME + " (" + name + ") VALUES ('" + nameValue + "');";
                db.execSQL(sqlQuery);

                getContext().getContentResolver().notifyChange(uri, null);

                return uri.buildUpon().appendPath(String.valueOf(ContentUris.parseId(uri))).build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    private boolean isSQLInjectionAttempt(String input) {
        String lowerCaseInp = input.toLowerCase();
        return lowerCaseInp.contains("' or ") || input.contains("' or a=a ") || input.contains("' or 1=1") || input.contains("' union select null");
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "EmpDB";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " name TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
