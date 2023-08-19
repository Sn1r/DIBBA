package com.example.dibba;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadcastReceiver", "Received Broadcast: " + intent.getAction());

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "The device is charging", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "The device is not charging", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "The battery is low", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals("com.example.dibba.CUSTOM_INTENT")) {
            String userMsg = intent.getStringExtra("msg");
            if (userMsg != null && md5Hash(userMsg).equals("6b1628b016dff46e6fa35684be6acc96")) {
                openSmsAppWithFlag(context);
            } else {
                CharSequence iData = intent.getCharSequenceExtra("msg");
                Toast.makeText(context, "Incorrect Message Received: " + iData, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openSmsAppWithFlag(Context context) {
        String flagMessage = "DIBBA{3xpl0it3d-Broadcast-R3c31v3r}";
        String phoneNumber = "+1123456";

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + phoneNumber));
        smsIntent.putExtra("sms_body", flagMessage);

        context.startActivity(smsIntent);
    }

    private String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
