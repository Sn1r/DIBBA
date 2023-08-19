package com.example.dibba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userp);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
    public void onClickAddDetails(View view) {
        try {
            ContentValues values = new ContentValues();
            values.put(UserProvider.name, ((EditText) findViewById(R.id.txtName)).getText().toString());
            Uri insertedUri = getContentResolver().insert(UserProvider.CONTENT_URI, values);

            if (insertedUri != null) {
                // Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_LONG).show();

                Cursor cursor = getContentResolver().query(insertedUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    StringBuilder result = new StringBuilder();
                    while (!cursor.isAfterLast()) {
                        int idIndex = cursor.getColumnIndex(UserProvider.id);
                        int nameIndex = cursor.getColumnIndex(UserProvider.name);
                        int recordId = cursor.getInt(idIndex);
                        String recordName = cursor.getString(nameIndex);
                        result.append("ID: ").append(recordId).append(", Name: ").append(recordName).append("\n");
                        cursor.moveToNext();
                    }
                    cursor.close();
                    TextView resultView = findViewById(R.id.res);
                    resultView.setText(result.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "User Added", Toast.LENGTH_LONG).show();
        }
    }
}