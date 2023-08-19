package com.example.dibba;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }
    // Start the service
    public void startService(View view) {
        Toast.makeText(this, "This button doesn't work at the moment", Toast.LENGTH_LONG).show();
    }
    // Stop the service
    public void stopService(View view) {
        stopService(new Intent(this, MyService.class));
    }
}