package com.example.hijaiyahku_new;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundSoundService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.test);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(0.5f, 0.5f); // Default volume
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if the intent has volume-related extras
        if (intent != null && intent.hasExtra("action")) {
            switch (intent.getStringExtra("action")) {
                case "setVolume":
                    float volume = intent.getFloatExtra("volume", 0.5f); // Default volume if not provided
                    setVolume(volume);
                    break;
                // Add other actions if needed
            }
        } else {
            // Your existing logic for starting the service and playing the background sound
            mediaPlayer.start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void setVolume(float volume) {
        // Make sure the volume is within the valid range (0.0 to 1.0)
        float validVolume = Math.max(0.0f, Math.min(1.0f, volume));

        // Adjust the volume of the media player
        mediaPlayer.setVolume(validVolume, validVolume);
    }
}
