package com.asdev.planetpaper.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SettingsUpdateReceiver extends BroadcastReceiver {

    public static final String INTENT_FILTER_ACTION = "PlanetPaper.SettingsUpdate";
    private final PlanetPaperWallpaperService.PlanetPaperWallpaperEngine engine;

    public SettingsUpdateReceiver(PlanetPaperWallpaperService.PlanetPaperWallpaperEngine engine) {
        this.engine = engine;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        engine.requiresSetup.set(true);
    }

}
