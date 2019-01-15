package com.asdev.planetpaper.wallpaper;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class DrawTimer implements Runnable {

    AtomicBoolean isRunning = new AtomicBoolean(false);

    private PlanetPaperWallpaperService.PlanetPaperWallpaperEngine engine;

    public DrawTimer(PlanetPaperWallpaperService.PlanetPaperWallpaperEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        while (true) {

            while(isRunning.get()) {
                engine.onUpdate();
                engine.onDraw();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }

            try {
                if(!isRunning.get())
                    Thread.sleep(1000000);
            } catch (InterruptedException e) {
                isRunning.set(true);
            }

        }
    }


}
