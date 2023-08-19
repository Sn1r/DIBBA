package com.example.dibba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class ImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.implicit_activity);

        Button b1 = (Button)findViewById(R.id.btn1);
        EditText etxt1 = (EditText)findViewById(R.id.etxt);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = etxt1.getText().toString().trim();
                if (!TextUtils.isEmpty(searchText)) {
                    String query = "Search for: " + searchText + "\nSensitive Data Leaked: DIBBA{D47a-L34k-7hr0ugh-Impl1c17-1nt3nt}";

                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), "Invalid data entered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}