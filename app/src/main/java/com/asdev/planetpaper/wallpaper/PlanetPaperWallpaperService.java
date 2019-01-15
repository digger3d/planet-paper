package com.asdev.planetpaper.wallpaper;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.asdev.planetpaper.wallpaper.animations.AnimationEncaspulator;
import com.asdev.planetpaper.wallpaper.animations.MidAnimationEncaspulator;
import com.asdev.planetpaper.wallpaper.animations.RevolvingAnimationEncaspulator;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlanetPaperWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new PlanetPaperWallpaperEngine();
    }


    class PlanetPaperWallpaperEngine extends WallpaperService.Engine implements GestureDetector.OnGestureListener {

        private PhoneUnlockReciever phoneUnlockReciever = new PhoneUnlockReciever();
        private SettingsUpdateReceiver settingsUpdateReceiver = new SettingsUpdateReceiver(this);
        private GestureDetectorCompat detectorCompat;

        private AnimationEncaspulator encaspulator;
        private DrawTimer timer = new DrawTimer(this);
        private Thread drawThread = new Thread(timer);

        protected AtomicBoolean requiresSetup = new AtomicBoolean(false);

        public PlanetPaperWallpaperEngine() {
            setTouchEventsEnabled(true);
            detectorCompat = new GestureDetectorCompat(getApplicationContext(), this);
            // IntentFilter filter = new IntentFilter();
            // filter.addAction(Intent.ACTION_USER_PRESENT);
            // filter.addAction(Intent.ACTION_SCREEN_OFF);
            // registerReceiver(phoneUnlockReciever, filter);

            setup();

            drawThread.start();

            IntentFilter intentFilter = new IntentFilter(SettingsUpdateReceiver.INTENT_FILTER_ACTION);
            registerReceiver(settingsUpdateReceiver, intentFilter);
        }

        public void setup() {
            Context c = getApplicationContext();
            // get the encaspulator
            String anim = PreferenceManager.getDefaultSharedPreferences(c).getString(getString(R.string.key_select_animation), "Mid");

            if(encaspulator != null) {
                encaspulator.onDestroy();
            }

            switch (anim) {

                case "Top/Static": {
                    encaspulator = new RevolvingAnimationEncaspulator(getApplicationContext(), false, false);
                    break;
                }

                case "Top/Revolving": {
                    encaspulator = new RevolvingAnimationEncaspulator(getApplicationContext(), true, false);
                    break;
                }

                case "Bottom/Static": {
                    encaspulator = new RevolvingAnimationEncaspulator(getApplicationContext(), false, true);
                    break;
                }

                case "Bottom/Revolving": {
                    encaspulator = new RevolvingAnimationEncaspulator(getApplicationContext(), true, true);
                    break;
                }

                default: {
                    encaspulator = new MidAnimationEncaspulator(getApplicationContext());
                    break;
                }

            }

            encaspulator.onConstruct();
            encaspulator.onSetup();
            encaspulator.onSurfaceChanged(w, h);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            detectorCompat.onTouchEvent(event);
            super.onTouchEvent(event);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if(visible) {
                if(requiresSetup.getAndSet(false)) {
                    setup();
                }

                encaspulator.onVisibilityChange(true);

                timer.isRunning.set(true);
                drawThread.interrupt();
            } else {
                encaspulator.onVisibilityChange(false);
                timer.isRunning.set(false);
            }
        }

        public void onUpdate() {
            encaspulator.onUpdate();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            // unregisterReceiver(phoneUnlockReciever);
            unregisterReceiver(settingsUpdateReceiver);
        }

        private int w, h;

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            super.onSurfaceChanged(holder, format, w, h);
            this.w = w;
            this.h = h;
            encaspulator.onSurfaceChanged(w, h);
        }

        protected void onDraw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = holder.lockCanvas();

            if(canvas != null) {
                encaspulator.onDraw(canvas);

                holder.unlockCanvasAndPost(canvas);
            }

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }
}
