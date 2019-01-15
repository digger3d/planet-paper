package com.asdev.planetpaper.wallpaper;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetWallpaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_wallpaper_activity);

        ((TextView) findViewById(R.id.description)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf"));
    }

    public void launchIntent(View v) {
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, PlanetPaperWallpaperService.class));
        startActivity(intent);
    }

    public void launchSettingsIntent(View view) {
        Intent intent = new Intent(this, WallpaperSettingsActivity.class);
        startActivity(intent);
    }
}
