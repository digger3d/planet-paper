package com.asdev.planetpaper.wallpaper.animations;

import android.graphics.Canvas;

public interface AnimationEncaspulator {

    void onConstruct();

    void onSetup();

    void onDestroy();

    void onVisibilityChange(boolean vis);

    void onSurfaceChanged(int w, int h);

    void onDraw(Canvas canvas);

    void onUpdate();
}
