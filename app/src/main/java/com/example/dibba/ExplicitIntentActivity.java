package com.example.dibba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExplicitIntentActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explicit_activity);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (isValidLogin(username, password)) {
                    Intent i = new Intent(ExplicitIntentActivity.this, SecondActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(ExplicitIntentActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isValidLogin(String username, String password) {
        String secureUsername = "superuser";
        String securePass = "MyP@ssw0rd123";

        return username.equals(secureUsername) && password.equals(securePass);

    }
}
