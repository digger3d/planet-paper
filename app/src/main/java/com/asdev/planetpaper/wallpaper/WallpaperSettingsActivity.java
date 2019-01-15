package com.asdev.planetpaper.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class WallpaperSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_settings);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new WallpaperPreferenceFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update the wallpaper activity
        Intent intent = new Intent(SettingsUpdateReceiver.INTENT_FILTER_ACTION);
        sendBroadcast(intent);
    }
}
