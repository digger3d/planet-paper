package com.asdev.planetpaper.wallpaper.animations;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.asdev.planetpaper.wallpaper.R;
import com.asdev.planetpaper.wallpaper.SyncAnimator;

public class MidAnimationEncaspulator implements AnimationEncaspulator {

    private static final float PADDING = 400f;
    private static final float FLING_OFFSET_Y = -100f, FLING_SCALE_ADD = PADDING / 16f;

    private Bitmap foregroundBitmap;
    private Rect foregroundRect;
    private RectF drawSector;
    private RectF centerDrawSector;

    private Bitmap lightMap;
    private Rect lightMapRect;
    private RectF lightMapDrawSector;
    private RectF lightMapOrgDrawSector;

    private SyncAnimator panAnim = new SyncAnimator(new LinearInterpolator());
    private SyncAnimator introAnim = new SyncAnimator(new DecelerateInterpolator(2.2f));

    private Context context;

    public MidAnimationEncaspulator(Context context) {
        this.context = context;
    }

    @Override
    public void onConstruct() {
        introAnim.setStartVal(0f);
        introAnim.setEndVal(FLING_OFFSET_Y);
        introAnim.setDuration(1200);

        panAnim.setStartVal(770f);
        panAnim.setEndVal(-770f);

        panAnim.setCurrentFraction(0.25f);
        panAnim.setDuration(10000);
        panAnim.setRepeating(true);
        panAnim.start();
    }

    @Override
    public void onSetup() {

        if(foregroundBitmap != null)
            foregroundBitmap.recycle();
        if(lightMap != null)
            lightMap.recycle();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String planet = prefs.getString(context.getString(R.string.key_selected_planet), "Mars");
        boolean hiRes = prefs.getBoolean(context.getString(R.string.key_hi_res_textures), false);

        String revolveTime = prefs.getString(context.getString(R.string.key_revolve_time), "30s");

        switch (revolveTime) {

            case "5s": {
                panAnim.setDuration(5000);
                break;
            }

            case "10s": {
                panAnim.setDuration(10000);
                break;
            }

            case "15s": {
                panAnim.setDuration(15000);
                break;
            }

            case "60s": {
                panAnim.setDuration(60000);
                break;
            }

            default: {
                panAnim.setDuration(30000);
                break;
            }
        }

        switch (planet) {

            case "Enceladus": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enceleadus_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enceleadus);
                break;
            }

            case "Europa": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.europa);
                break;
            }

            case "The Moon": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.moon_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.moon);
                break;
            }

            case "Ganymede": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ganymede_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ganymede);
                break;
            }

            case "Io": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.io_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.io);
                break;
            }

            case "Triton": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.triton_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.triton);
                break;
            }

            case "Mercury": {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mercury_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mercury);
                break;
            }

            case "Warm Sun": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_2_2x);

                break;
            }

            case "Winter Sun": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_1_2x);

                break;
            }

            case "Yellow Sun": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_5_2x);

                break;
            }

            default: {
                if(hiRes)
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mars_2x);
                else
                    foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mars);
                break;
            }
        }

        if(hiRes)
            lightMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light_map_2x);
        else
            lightMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light_map);


        foregroundRect = new Rect(0, 0, foregroundBitmap.getWidth(), foregroundBitmap.getHeight());
        lightMapRect = new Rect(0, 0, lightMap.getWidth(), lightMap.getHeight());
    }

    public void onDestroy() {
        foregroundBitmap.recycle();
        lightMap.recycle();
    }

    @Override
    public void onVisibilityChange(boolean vis) {
        if(vis) {
            introAnim.start(25);
            panAnim.setCurrentFraction(0.25f);
            panAnim.start(100);
        } else {
            panAnim.stop();
            introAnim.stop();
        }
    }

    @Override
    public void onSurfaceChanged(int w, int h) {
        float size = Math.min(w, h) - PADDING;
        // center the image within the screen
        centerDrawSector = new RectF((w - size) / 2f, (h - size) / 2f, size + (w - size) / 2f, size + (h - size) / 2f);
        drawSector = new RectF(centerDrawSector);
        // scale the shadow up a bit
        size *= 3.3f;
        lightMapOrgDrawSector = new RectF((w - size) / 2f, (h - size) / 2f, size + (w - size) / 2f, size + (h - size) / 2f);
        lightMapDrawSector = new RectF(lightMapOrgDrawSector);
        translateRect(lightMapDrawSector, 0f, -100f);
    }

    public void onUpdate() {
        if(introAnim.isRunning())
            onAnimationUpdate(introAnim.getAnimatedFraction(), introAnim.getValue());
    }

    @Override
    public void onDraw(Canvas canvas) {
        // draw the background
        canvas.drawRGB(0, 0, 0);

        // draw the foreground bitmap
        canvas.drawBitmap(foregroundBitmap, foregroundRect, drawSector, null);
        canvas.translate(panAnim.getValue(), 0f);
        canvas.drawBitmap(lightMap, lightMapRect, lightMapDrawSector, null);
        canvas.translate(panAnim.getValue(), 0f);
    }

    private void onAnimationUpdate(float fraction, float value) {
        float sizeDiff = fraction * FLING_SCALE_ADD;
        drawSector.left = centerDrawSector.left - sizeDiff;
        drawSector.right = centerDrawSector.right + sizeDiff;
        drawSector.top = centerDrawSector.top + value - sizeDiff;
        drawSector.bottom = centerDrawSector.bottom + value + sizeDiff;

        sizeDiff *= 7f;

        lightMapDrawSector.left = lightMapOrgDrawSector.left - sizeDiff;
        lightMapDrawSector.right = lightMapOrgDrawSector.right + sizeDiff;
        lightMapDrawSector.top = lightMapOrgDrawSector.top + value - sizeDiff;
        lightMapDrawSector.bottom = lightMapOrgDrawSector.bottom + value + sizeDiff;

        translateRect(lightMapDrawSector, 0f, -100f);
    }


    public static void translateRect(RectF rectF, float x, float y) {
        rectF.left += x;
        rectF.right += x;
        rectF.top += y;
        rectF.bottom += y;
    }

}
