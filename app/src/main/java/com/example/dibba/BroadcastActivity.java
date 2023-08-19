package com.example.dibba;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class BroadcastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        // Create the broadcast receiver
        MyBroadcastReceiver myReceiver = new MyBroadcastReceiver();

        // Register the broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.dibba.CUSTOM_INTENT");
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        registerReceiver(myReceiver, filter);
    }
    public void onClickShowBroadcast(View view){
        EditText st = findViewById(R.id.txtMsg);
        String message = st.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("msg", (CharSequence)message);
        intent.setAction("com.example.dibba.CUSTOM_INTENT");
        sendBroadcast(intent);
    }
}