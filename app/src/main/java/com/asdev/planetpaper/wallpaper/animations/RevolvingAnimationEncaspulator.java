package com.asdev.planetpaper.wallpaper.animations;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.asdev.planetpaper.wallpaper.R;
import com.asdev.planetpaper.wallpaper.SyncAnimator;

public class RevolvingAnimationEncaspulator implements AnimationEncaspulator {

    private static float OVER_DRAW = 150f, OFFSET_Y = 600f, FLING_OFFSET_Y = 200f, SCALE_CHANGE = -100f;

    private final Context context;
    private final boolean revolving;
    private final boolean isBottom;

    private Bitmap foregroundBitmap;
    private Rect foregroundRect;

    private RectF drawSector;
    private RectF drawSectorOrg;
    private RectF lightDrawSector;
    private RectF lightDrawSectorOrg;

    private SyncAnimator introAnim = new SyncAnimator(new DecelerateInterpolator(2.2f));
    private SyncAnimator revolveAnim = new SyncAnimator(new LinearInterpolator());

    private Bitmap lightMap;
    private Rect lightMapRect;

    private Paint bgPaint = new Paint(Color.BLACK);
    // private Paint fgPaint = new Paint();

    private boolean drawLightMap = true;

    private int gradientStart = Color.BLACK, gradientEnd = Color.BLACK;

    public RevolvingAnimationEncaspulator(Context context, boolean revolving, boolean isBottom) {
        this.context = context;
        this.revolving = revolving;
        this.isBottom = isBottom;

        if(isBottom) {
            FLING_OFFSET_Y = Math.abs(FLING_OFFSET_Y);
        } else {
            FLING_OFFSET_Y = -Math.abs(FLING_OFFSET_Y);
        }
    }

    @Override
    public void onConstruct() {
        revolveAnim.setDuration(150000);
        revolveAnim.setRepeating(true);
        revolveAnim.setStartVal(0f);
        revolveAnim.setEndVal(360f);
        if(revolving)
            revolveAnim.start();

        introAnim.setStartVal(FLING_OFFSET_Y);
        introAnim.setEndVal(0f);
        introAnim.setDuration(1600);
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

        String overDraw = prefs.getString(context.getString(R.string.key_over_draw), "150");
        try {
            OVER_DRAW = Float.parseFloat(overDraw);
        } catch (NumberFormatException nfe) { OVER_DRAW = 150f; }

        String offsetY = prefs.getString(context.getString(R.string.key_offset_y), "600");
        try {
            OFFSET_Y = Float.parseFloat(offsetY);
        } catch (NumberFormatException nfe) { OFFSET_Y = 600f; }

        drawLightMap = true;
        bgPaint.setColor(Color.BLACK);
        bgPaint.setShader(null);
        gradientStart = Color.BLACK;
        gradientEnd = Color.BLACK;

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
                drawLightMap = false;

                gradientStart = Color.parseColor("#a6b6ab");
                gradientEnd = Color.parseColor("#829395");

                break;
            }

            case "Winter Sun": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_1_2x);
                drawLightMap = false;

                gradientStart = Color.parseColor("#74778a");
                gradientEnd = Color.parseColor("#484a56");

                break;
            }

            case "Yellow Sun": {
                foregroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sun_5_2x);
                drawLightMap = false;

                gradientStart = Color.parseColor("#2e0807");
                gradientEnd = Color.parseColor("#000000");

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

        if(drawLightMap) {
            if (hiRes)
                lightMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light_map_radial_2x);
            else
                lightMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.light_map_radial);
        }

        foregroundRect = new Rect(0, 0, foregroundBitmap.getWidth(), foregroundBitmap.getHeight());
        if(drawLightMap) {
            lightMapRect = new Rect(0, 0, lightMap.getWidth(), lightMap.getHeight());
        }
    }

    @Override
    public void onDestroy() {
        foregroundBitmap.recycle();
        if(lightMap != null)
            lightMap.recycle();
    }

    @Override
    public void onVisibilityChange(boolean vis) {
        if(vis) {
            if(revolving)
                revolveAnim.start();
            introAnim.start();
        } else {
            if(revolving)
                revolveAnim.stop();
        }
    }

    @Override
    public void onSurfaceChanged(int w, int h) {
        if(gradientStart != Color.BLACK) {
            int x = (int) ( ((double) w) / 2.0 );
            if(!isBottom) {
                bgPaint.setShader(new LinearGradient(x, 0, x, h, gradientStart, gradientEnd, Shader.TileMode.CLAMP));
            } else {
                bgPaint.setShader(new LinearGradient(x, 0, x, h, gradientEnd, gradientStart, Shader.TileMode.CLAMP));
            }
        }

        drawSectorOrg = new RectF();
        float size = w + OVER_DRAW * 2f;
        drawSectorOrg.left = -OVER_DRAW;
        drawSectorOrg.right = w + OVER_DRAW;
        if(!isBottom) {
            drawSectorOrg.top = -size + OFFSET_Y;
            drawSectorOrg.bottom = drawSectorOrg.top + size;
        } else {
            drawSectorOrg.bottom = h + size - OFFSET_Y;
            drawSectorOrg.top = drawSectorOrg.bottom - size;
        }

        drawSector = new RectF(drawSectorOrg);

        if(drawLightMap) {
            lightDrawSectorOrg = new RectF(drawSectorOrg);

            lightDrawSectorOrg.left -= OVER_DRAW;
            lightDrawSectorOrg.right += OVER_DRAW;

            lightDrawSector = new RectF(lightDrawSectorOrg);
        }

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPaint(bgPaint);
        if(revolving)
            canvas.rotate(revolveAnim.getValue(), drawSector.centerX(), drawSector.centerY());
        canvas.drawBitmap(foregroundBitmap, foregroundRect, drawSector, /* fgPaint */ null);
        if(revolving)
            canvas.rotate(-revolveAnim.getValue(), drawSector.centerX(), drawSector.centerY());
        if(drawLightMap)
            canvas.drawBitmap(lightMap, lightMapRect, lightDrawSector, null);
    }

    @Override
    public void onUpdate() {
        if(introAnim.isRunning()) {

            float sizeDiff = (1f - introAnim.getAnimatedFraction()) * SCALE_CHANGE;

            drawSector.top = drawSectorOrg.top + introAnim.getValue() - sizeDiff;
            drawSector.bottom = drawSectorOrg.bottom + introAnim.getValue() + sizeDiff;
            drawSector.left = drawSectorOrg.left - sizeDiff;
            drawSector.right = drawSectorOrg.right + sizeDiff;

            // fgPaint.setAlpha((int)(introAnim.getAnimatedFraction() * 155) + 100);
        }
    }
}
