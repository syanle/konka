package com.cyanogenmod.trebuchet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CommonReceiver extends BroadcastReceiver {
    public static final String ACTION_UPDATE_AD_PIC = "com.konka.launcher.action.UPDATE_AD_PIC";
    private static final String TAG = "CommonReceiver";

    public void onReceive(Context context, Intent data) {
        String action = data.getAction();
        Log.d(TAG, "receive broadcast ===" + action);
        if ("com.konka.action.USER_SWITCHED".equals(action)) {
            Launcher.setScreenLockState(false);
            context.sendBroadcast(new Intent("konka.action.SCREEN_UNLOCK"));
        }
    }
}
