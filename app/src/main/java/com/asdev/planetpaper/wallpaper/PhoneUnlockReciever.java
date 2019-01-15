package com.asdev.planetpaper.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhoneUnlockReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.d("PlanetPaper", "Phone unlocked");
        }else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d("PlanetPaper", "Phone locked");
        }
    }

}
