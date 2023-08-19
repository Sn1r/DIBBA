package com.example.dibba;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import java.util.Locale;


public class MyService extends Service implements OnInitListener {
    private MediaPlayer player;
    private TextToSpeech textToSpeech;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Toast.makeText(this, "TTS Language is not supported.", Toast.LENGTH_SHORT).show();
            } else {
                float speechRate = 0.6f;
                textToSpeech.setSpeechRate(speechRate);

                String flagToRead = "You found the secret flag via the exported service, well done";
                textToSpeech.speak(flagToRead, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            Toast.makeText(this, "TTS Initialization failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPlaying = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        textToSpeech = new TextToSpeech(this, this);

        if (!isPlaying) {
            // Initialize TTS
            textToSpeech = new TextToSpeech(this, this);
            isPlaying = true;
        }

        Toast.makeText(this, "DIBBA{Expl0171n9-ExP0R7ED-5erv1Ce2}", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        isPlaying = false;

        // Stopping the player when service is destroyed
        if (player != null) {
            player.stop();
        }
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }
}