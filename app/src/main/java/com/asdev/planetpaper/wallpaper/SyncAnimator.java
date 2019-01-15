package com.asdev.planetpaper.wallpaper;

import android.view.animation.Interpolator;

public class SyncAnimator {

    private long startTime = -1L;
    private boolean isRunning = false;
    private long duration = -1L;
    private Interpolator interpolator;

    private float startVal = 0f, endVal = 0f;
    private boolean isRepeating = false;
    private float currentFraction;

    public SyncAnimator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setStartVal(float startVal) {
        this.startVal = startVal;
    }

    public float getStartVal() {
        return startVal;
    }

    public void setEndVal(float endVal) {
        this.endVal = endVal;
    }

    public float getEndVal() {
        return endVal;
    }

    private float getTime() {
        return (System.nanoTime() - startTime + 0.0f) / (duration + 0.0f) + currentFraction;
    }

    public float getAnimatedFraction() {
        if(isRepeating()) {
            return interpolator.getInterpolation(Math.max(getTime(), 0f) % 1f);
        } else {
            return interpolator.getInterpolation(Math.max(Math.min(getTime(), 1f), 0f));
        }
    }

    public void setDuration(long duration) {
        this.duration = duration * 1000000L;
    }

    public long getDuration() {
        return duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public float getValue() {
        if(!isRunning()) {
            return startVal;
        }

        return startVal + (endVal - startVal) * getAnimatedFraction();
    }

    public void start() {
        startTime = System.nanoTime();
        isRunning = true;
    }

    public void start(int startDelay) {
        startTime = System.nanoTime() + startDelay * 1000000L;
        isRunning = true;
    }

    public void stop() {
        startTime = -1L;
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public void setRepeating(boolean repeating) {
        isRepeating = repeating;
    }

    public void setCurrentFraction(float currentFraction) {
        this.currentFraction = currentFraction;
    }
}
